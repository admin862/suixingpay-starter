package com.suixingpay.takin.web.demo;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.suixingpay.takin.data.enums.BaseEnum;
import com.suixingpay.takin.web.BaseController;

@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
@RestController
public class WebDemoApplication extends BaseController {
    private static final String CN_DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    public static void main(String[] args) {
        SpringApplication.run(WebDemoApplication.class, args);
    }

    @GetMapping("/date")
    public String dateTest(@RequestParam Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat(CN_DATE_TIME_FORMAT);
        return toJson(formatter.format(date));
    }

    @GetMapping("/date2")
    public String dateTest2(@RequestParam @DateTimeFormat(pattern = "yyyyMMdd") Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat(CN_DATE_TIME_FORMAT);
        return toJson(formatter.format(date));
    }

    @GetMapping("/enum")
    public Sex enumTest(@RequestParam Sex sex) {
        return sex;
    }

    @GetMapping("/enum2")
    public Sex2 enumTest2(@RequestParam Sex2 sex2) {
        return sex2;
    }

    static enum Sex implements BaseEnum {
        MAN(1, "男性"),
        WOMAN(2, "女性");

        private int code;
        private String displayName;

        private Sex(int code, String displayName) {
            this.code = code;
            this.displayName = displayName;
        }

        @Override
        public int getCode() {
            return code;
        }

        @Override
        public String getDisplayName() {
            return displayName;
        }
    }

    static enum Sex2 {
        MAN(1, "男性"),
        WOMAN(2, "女性");

        private int code;
        private String displayName;

        private Sex2(int code, String displayName) {
            this.code = code;
            this.displayName = displayName;
        }

        public int getCode() {
            return code;
        }

        public String getDisplayName() {
            return displayName;
        }

    }
}
