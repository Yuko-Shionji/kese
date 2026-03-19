package com.ruralwater.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 简易日志工具类
 * 提供基本的日志记录功能，无需额外依赖
 */
public class SimpleLogger {
    
    // 日志级别
    public enum Level {
        DEBUG, INFO, WARN, ERROR
    }
    
    private static final String LOG_DIR = "logs";
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static final SimpleDateFormat fileDateFormat = new SimpleDateFormat("yyyy-MM-dd");
    
    // 是否启用日志
    private static boolean enabled = true;
    
    // 最低日志级别
    private static Level minLevel = Level.INFO;
    
    /**
     * 设置日志级别
     * @param level 最低日志级别
     */
    public static void setLogLevel(Level level) {
        minLevel = level;
    }
    
    /**
     * 启用日志
     */
    public static void enable() {
        enabled = true;
    }
    
    /**
     * 禁用日志
     */
    public static void disable() {
        enabled = false;
    }
    
    /**
     * 输出调试日志
     */
    public static void debug(String message) {
        log(Level.DEBUG, message, null);
    }
    
    /**
     * 输出调试日志（带异常）
     */
    public static void debug(String message, Throwable t) {
        log(Level.DEBUG, message, t);
    }
    
    /**
     * 输出信息日志
     */
    public static void info(String message) {
        log(Level.INFO, message, null);
    }
    
    /**
     * 输出信息日志（带异常）
     */
    public static void info(String message, Throwable t) {
        log(Level.INFO, message, t);
    }
    
    /**
     * 输出警告日志
     */
    public static void warn(String message) {
        log(Level.WARN, message, null);
    }
    
    /**
     * 输出警告日志（带异常）
     */
    public static void warn(String message, Throwable t) {
        log(Level.WARN, message, t);
    }
    
    /**
     * 输出错误日志
     */
    public static void error(String message) {
        log(Level.ERROR, message, null);
    }
    
    /**
     * 输出错误日志（带异常）
     */
    public static void error(String message, Throwable t) {
        log(Level.ERROR, message, t);
    }
    
    /**
     * 核心日志方法
     */
    private static void log(Level level, String message, Throwable t) {
        if (!enabled || level.ordinal() < minLevel.ordinal()) {
            return;
        }
        
        String timestamp = dateFormat.format(new Date());
        String logMessage = String.format("[%s] [%s] %s", timestamp, level, message);
        
        // 控制台输出
        System.out.println(logMessage);
        if (t != null) {
            t.printStackTrace(System.out);
        }
        
        // 异步写入文件（不阻塞主线程）
        writeToFileAsync(logMessage, t);
    }
    
    /**
     * 异步写入日志文件
     */
    private static void writeToFileAsync(final String logMessage, final Throwable t) {
        new Thread(() -> {
            try {
                writeToFile(logMessage, t);
            } catch (Exception e) {
                // 静默失败，避免无限递归
            }
        }).start();
    }
    
    /**
     * 写入日志文件
     */
    private static void writeToFile(String logMessage, Throwable t) throws IOException {
        // 确保日志目录存在
        File dir = new File(LOG_DIR);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        
        // 日志文件名（按日期）
        String fileName = "log_" + fileDateFormat.format(new Date()) + ".txt";
        File logFile = new File(LOG_DIR, fileName);
        
        // 追加写入
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(logFile, true))) {
            writer.write(logMessage);
            writer.newLine();
            
            if (t != null) {
                writer.write(getStackTrace(t));
                writer.newLine();
            }
        }
    }
    
    /**
     * 获取异常堆栈跟踪字符串
     */
    private static String getStackTrace(Throwable t) {
        StringBuilder sb = new StringBuilder();
        for (StackTraceElement element : t.getStackTrace()) {
            sb.append("\tat ").append(element.toString()).append("\n");
        }
        return sb.toString();
    }
}
