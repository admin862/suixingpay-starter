package com.suixingpay.takin.token;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Optional;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.util.StringUtils;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.RequestAttributes;

import com.suixingpay.takin.token.repository.client.TokenClientRepository;
import com.suixingpay.takin.token.repository.server.ServerTokenRepository;
import com.suixingpay.takin.token.repository.server.UserTokenRepository;
import com.suixingpay.takin.token.util.MessageDigestUtil;
import com.suixingpay.takin.token.util.MiscUtil;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * 登录token生成工具
 *
 * @author jiayu.qiu
 */
@Slf4j
@Data
public class TokenHelper {

    private static final String CURRENT_TOKEN_ATTRIBUTE_NAME = "SXF_CURRENT_TOKEN";

    private final ServerTokenRepository<TokenInfo> serverTokenRepository;

    private final UserTokenRepository userTokenRepository;

    private final TokenClientRepository tokenClientRepository;

    private final TokenProperties tokenProperties;

    public TokenHelper(ServerTokenRepository<TokenInfo> serverTokenRepository, UserTokenRepository userTokenRepository,
            TokenClientRepository tokenClientRepository, TokenProperties tokenProperties) {
        this.serverTokenRepository = serverTokenRepository;
        this.userTokenRepository = userTokenRepository;
        this.tokenClientRepository = tokenClientRepository;
        this.tokenProperties = tokenProperties;
    }

    /**
     * 生成基础的登录 Token
     *
     * @param applicationName
     * @param userId 用户id
     * @return 返回Token
     */
    public String genToken(Object userId) {
        if (null == userId) {
            throw new IllegalArgumentException("userId 不能为null");
        }
        StringBuilder tokenBuilder = new StringBuilder();
        // 应用名称
        tokenBuilder.append(tokenProperties.getNamespace())
                // 用户ID
                .append(String.valueOf(userId))
                // 时间戳
                .append(System.currentTimeMillis());
        int maxLen = 50;
        int minLen = 5;
        int len = maxLen - tokenBuilder.length();
        if (len < minLen) {
            len = minLen;
        }
        tokenBuilder.insert(tokenBuilder.length() / 2, MiscUtil.getRandomStr(len, true));
        // 为了安全起见，进行二次MD5
        StringBuilder tokenBuilder2 = new StringBuilder();
        tokenBuilder2.append(MessageDigestUtil.getSHA512(tokenBuilder.toString()))
                .append(MiscUtil.getRandomStr(minLen, true));
        return MessageDigestUtil.getMD5(tokenBuilder2.toString());
    }

    /**
     * 设置当前登录用户
     *
     * @param tokenWapper
     */
    private static void setTokenWapper(HttpServletRequest request, Optional<TokenWapper> tokenWapper) {
        request.setAttribute(CURRENT_TOKEN_ATTRIBUTE_NAME, tokenWapper);
    }

    /**
     * 获取当前登录用户
     *
     * @return
     */
    public Optional<TokenWapper> getTokenWapper(HttpServletRequest request) {
        @SuppressWarnings("unchecked")
        Optional<TokenWapper> tokenWapperOpt = (Optional<TokenWapper>) request
                .getAttribute(CURRENT_TOKEN_ATTRIBUTE_NAME);
        if (null != tokenWapperOpt) {
            return tokenWapperOpt;
        }
        tokenWapperOpt = Optional.empty();
        try {
            // 校验token
            String token = tokenClientRepository.get(request, tokenProperties.getTokenName());
            log.debug("传入的token值:{}", token);
            if (StringUtils.isEmpty(token)) {
                log.debug("当前请求token为is empty");
                return tokenWapperOpt;
            }
            TokenInfo tokenInfo = serverTokenRepository.get(token);
            if (null == tokenInfo) {
                log.debug("token过期或已失效");
                return tokenWapperOpt;
            }
            if (isTimeout(tokenInfo)) {
                return tokenWapperOpt;
            }
            TokenWapper tokenWapper = new TokenWapper(token, tokenInfo);
            tokenWapperOpt = Optional.of(tokenWapper);
            return tokenWapperOpt;
        } finally {
            setTokenWapper(request, tokenWapperOpt);
        }
    }

    private boolean isTimeout(TokenInfo tokenInfo) {
        if (null == tokenInfo) {
            return true;
        }
        long timeout = (System.currentTimeMillis() - tokenInfo.getCreateTime()) / 1000;
        if (timeout > tokenProperties.getMaxTimeout()) {
            log.debug("token已经超过{}秒", timeout);
            return true;
        }
        return false;
    }

    /**
     * 获取当前登录用户
     *
     * @return
     */
    public static Optional<TokenWapper> getTokenWapper(NativeWebRequest request) {
        @SuppressWarnings("unchecked")
        Optional<TokenWapper> tokenWapperOpt = (Optional<TokenWapper>) request
                .getAttribute(CURRENT_TOKEN_ATTRIBUTE_NAME, RequestAttributes.SCOPE_REQUEST);
        if (null != tokenWapperOpt) {
            return tokenWapperOpt;
        }
        return Optional.empty();
    }

    /**
     * 登录成功，把登录信息保存到服务器仓库中
     *
     * @param response
     * @param tokenInfo
     * @return
     */
    public String login(HttpServletResponse response, TokenInfo tokenInfo) {
        if(null == tokenInfo) {
            throw new IllegalArgumentException("tokenInfo 不能为null");
        }
        String userId = tokenInfo.getUserId();
        if (null == userId) {
            throw new IllegalArgumentException("userId 不能为null");
        }
        TokenProperties tokenProperties = getTokenProperties();
        boolean singleMode = tokenProperties.isSingleMode();
        String token = genToken(userId);
        tokenInfo.setUserId(String.valueOf(userId));
        // 设置创建时间
        tokenInfo.setCreateTime(System.currentTimeMillis());
        this.serverTokenRepository.set(token, tokenInfo, tokenProperties.getTimeout());
        if (singleMode) {
            String oldToken = this.userTokenRepository.getSet(String.valueOf(userId), token,
                    tokenProperties.getMaxTimeout());
            if (null != oldToken) {
                this.serverTokenRepository.del(oldToken);
            }
        } else {
            cleanTimeoutToken(userId);
            this.userTokenRepository.addToken(String.valueOf(userId), token, tokenProperties.getMaxTimeout());
        }
        this.tokenClientRepository.set(response, tokenProperties.getTokenName(), token);
        return token;
    }

    /**
     * 登出
     *
     * @param request
     * @param response
     * @return
     */
    public boolean logout(HttpServletRequest request, HttpServletResponse response) {
        String tokenName = tokenProperties.getTokenName();
        String token = tokenClientRepository.get(request, tokenName);
        if (null == token || token.isEmpty()) {
            return false;
        }
        Optional<TokenWapper> tokenWapperOpt = getTokenWapper(request);
        if (!tokenWapperOpt.isPresent()) {
            return false;
        }
        String userId = tokenWapperOpt.get().getTokenInfo().getUserId();
        if (tokenProperties.isSingleMode()) {
            if (this.userTokenRepository.del(userId) && this.serverTokenRepository.del(token)) {
                tokenClientRepository.del(request, response, tokenName);
                return true;
            }
        } else {
            if (this.userTokenRepository.removeToken(userId, token) && this.serverTokenRepository.del(token)) {
                tokenClientRepository.del(request, response, tokenName);
                return true;
            }
        }
        return false;
    }

    /**
     * 清除该用户所有Token
     *
     * @param userId
     * @return
     */
    public void logoutAll(Object userId) {
        if (null == userId) {
            throw new IllegalArgumentException("userId 不能为null");
        }
        if (tokenProperties.isSingleMode()) {
            String oldToken = this.userTokenRepository.getSingleModeToken(String.valueOf(userId));
            if (null != oldToken) {
                this.serverTokenRepository.del(oldToken);
            }
            this.userTokenRepository.del(String.valueOf(userId));
        } else {
            Set<String> tokenSet = this.userTokenRepository.getAll(String.valueOf(userId));
            if (null != tokenSet && tokenSet.size() > 0) {
                for (String oldToken : tokenSet) {
                    if (null != oldToken) {
                        this.serverTokenRepository.del(oldToken);
                    }
                }
                this.userTokenRepository.del(String.valueOf(userId));
            }
        }
    }

    /**
     * 清除在此时间段之前的token
     *
     * @param userId
     * @return
     */
    private void cleanTimeoutToken(Object userId) {
        if (null == userId) {
            return;
        }
        Set<String> allTokenSet = this.userTokenRepository.getAll(String.valueOf(userId));
        if (null == allTokenSet || allTokenSet.isEmpty()) {
            return;
        }
        Set<String> needRemoveTokens = new HashSet<>();
        Iterator<String> allToken = allTokenSet.iterator();
        // 清除已过期或已失效的token
        while (allToken.hasNext()) {
            String token = allToken.next();
            TokenInfo tokenInfo = this.serverTokenRepository.get(token);
            if (null == tokenInfo) {
                needRemoveTokens.add(token);
                allToken.remove();
            } else if (isTimeout(tokenInfo)) {
                this.serverTokenRepository.del(token);
                needRemoveTokens.add(token);
                allToken.remove();
            }
        }
        // 控制当前在线用户数
        int allTokenSize = allTokenSet.size();
        if (allTokenSize >= this.tokenProperties.getMaxTokensForOneUser()) {
            int count = 0;
            for (String token : allTokenSet) {
                if (allTokenSize - count >= this.tokenProperties.getMaxTokensForOneUser()) {
                    this.serverTokenRepository.del(token);
                    needRemoveTokens.add(token);
                    count++;
                }
            }
        }
        if (needRemoveTokens.size() > 0) {
            Long count = this.userTokenRepository.removeTokens(String.valueOf(userId), needRemoveTokens);
            log.debug("清除长时间未激活token，数量：{}", count);
        }
    }

    /**
     * 刷新token
     *
     * @param token
     */
    public void refresh(String token) {
        this.serverTokenRepository.expire(token, this.getTokenProperties().getTimeout());
    }
}
