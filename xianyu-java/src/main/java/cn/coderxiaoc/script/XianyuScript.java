package cn.coderxiaoc.script;

import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.*;

public class XianyuScript {

    private static final String SCRIPT_NAME = "xianyu_js_version_2.exe";

    public static Map<String, String> transCookies(String cookiesStr) {
        Map<String, String> cookies = new HashMap<>();
        String[] pairs = cookiesStr.split("; ");
        for (String pair : pairs) {
            String[] parts = pair.split("=", 2);
            if (parts.length == 2) {
                cookies.put(parts[0], parts[1]);
            }
        }
        return cookies;
    }

    public static String generateMid() {
        return runScript("generate_mid");
    }

    public static String generateUuid() {
        return runScript("generate_uuid");
    }

    public static String generateDeviceId(String userId) {
        return runScript("generate_device_id", userId);
    }

    public static String generateSign(String t, String token, String data) {
        return runScript("generate_sign", t, token, data);
    }

    public static String decrypt(String data) {
        return runScript("decrypt", data);
    }

    private static String runScript(String... args) {
        try {
            URL resource = XianyuScript.class.getClassLoader().getResource(SCRIPT_NAME);
            if (resource == null) {
                throw new FileNotFoundException("Script file not found: " + SCRIPT_NAME);
            }

            // 读取资源为流
            try (InputStream input = resource.openStream()) {
                // 创建临时可执行文件
                String suffix = SCRIPT_NAME.contains(".") ? SCRIPT_NAME.substring(SCRIPT_NAME.lastIndexOf(".")) : null;
                File tempScript = File.createTempFile("xianyu-script-", suffix);
                tempScript.deleteOnExit(); // 程序退出时自动删除
                Files.copy(input, tempScript.toPath(), StandardCopyOption.REPLACE_EXISTING);
                tempScript.setExecutable(true); // 设置可执行权限

                String exePath = tempScript.getAbsolutePath();

                // 构造命令行
                List<String> command = new ArrayList<>();
                command.add(exePath);
                command.addAll(Arrays.asList(args));

                ProcessBuilder processBuilder = new ProcessBuilder(command);
                processBuilder.redirectErrorStream(true);

                Process process = processBuilder.start();
                StringBuilder output = new StringBuilder();

                try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream(), StandardCharsets.UTF_8))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        output.append(line);
                    }
                }

                int exitCode = process.waitFor();
                if (exitCode != 0) {
                    System.out.println("Script execution failed with exit code: " + exitCode);
                    return null;
                }

                return output.toString();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }
}
