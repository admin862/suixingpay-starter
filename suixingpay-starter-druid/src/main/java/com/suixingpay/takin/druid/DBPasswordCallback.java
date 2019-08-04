package com.suixingpay.takin.druid;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.util.Base64;
import java.util.Properties;

import com.alibaba.druid.support.logging.Log;
import com.alibaba.druid.support.logging.LogFactory;
import com.alibaba.druid.util.DruidPasswordCallback;
import com.alibaba.druid.util.StringUtils;

/**
 * @author Tieli.Ma
 * @description: DBPasswordCallback.java
 */
public class DBPasswordCallback extends DruidPasswordCallback {

    private static final Log LOG = LogFactory.getLog(DBPasswordCallback.class);

    private static final long serialVersionUID = 8352871450818066381L;

    @Override
    public void setProperties(Properties properties) {
        super.setProperties(properties);
        String pwd = properties.getProperty("password");

        if (!StringUtils.isEmpty(pwd)) {
            try {
                String enpassword = DESUtil.decryptHex(getKey(DBPasswordCallback.class.getResourceAsStream("/sxf_druid_key.properties")), pwd);
                setPassword(enpassword.toCharArray());
            } catch (Exception e) {
                LOG.error("decrypt password error:", e);
                throw new BadPasswordException("decryptFlag password error");
            }
        }
    }

    private static String getKey(InputStream in) {
        String str = null;
        try {
            ObjectInputStream objectInputStream = new ObjectInputStream(in);
            str = new String(Base64.getDecoder().decode((byte[]) objectInputStream.readObject()));
        } catch (FileNotFoundException e) {
            LOG.error("decrypt password getKey FileNotFoundException:", e);
        } catch (IOException e) {
            LOG.error("decrypt password getKey IOException:", e);
        } catch (ClassNotFoundException e) {
            LOG.error("decrypt password getKey ClassNotFoundException:", e);
        }
        return str;
    }
}
