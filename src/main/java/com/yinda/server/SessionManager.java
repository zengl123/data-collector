package com.yinda.server;

import com.yinda.model.dto.Session;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

/**
 * Copyright (C), 2018-2019, 湖南金峰信息科技有限公司
 *
 * @Description:
 * @Author:zengling
 * @钉钉:17363645521
 * @CreateDate:2019/5/30 16:38
 * @UpdateUser:
 * @UpdateDate:2019/5/30 16:38
 * @UpdateRemark:
 * @Version:
 */
public class SessionManager {
    private static volatile SessionManager instance = null;
    /**
     * netty生成的sessionID和Session的对应关系
     */
    private Map<String, Session> sessionIdMap;
    /**
     * 终端手机号和netty生成的sessionID的对应关系
     */
    private Map<String, String> phoneMap;

    public static SessionManager getInstance() {
        if (instance == null) {
            synchronized (SessionManager.class) {
                if (instance == null) {
                    instance = new SessionManager();
                }
            }
        }
        return instance;
    }

    public SessionManager() {
        this.sessionIdMap = new ConcurrentHashMap<>();
        this.phoneMap = new ConcurrentHashMap<>();
    }

    public boolean containsKey(String sessionId) {
        return sessionIdMap.containsKey(sessionId);
    }

    public boolean containsSession(Session session) {
        return sessionIdMap.containsValue(session);
    }

    public Session findBySessionId(String id) {
        return sessionIdMap.get(id);
    }

    public Session findByTerminalPhone(String phone) {
        phone = String.format("%016d", Long.parseLong(phone));
        String sessionId = this.phoneMap.get(phone);
        if (sessionId == null) {
            return null;
        }
        return this.findBySessionId(sessionId);
    }

    public synchronized Session put(String key, Session value) {
        if (value.getTerminalPhone() != null && !"".equals(value.getTerminalPhone().trim())) {
            String phone = value.getTerminalPhone();
            phone = String.format("%016d", Long.parseLong(phone));
            this.phoneMap.put(phone, value.getId());
        }
        return sessionIdMap.put(key, value);
    }

    public synchronized Session removeBySessionId(String sessionId) {
        if (sessionId == null) {
            return null;
        }
        Session session = sessionIdMap.remove(sessionId);
        if (session == null) {
            return null;
        }
        if (session.getTerminalPhone() != null) {
            String phone = session.getTerminalPhone();
            phone = String.format("%016d", Long.parseLong(phone));
            this.phoneMap.remove(phone);
        }
        return session;
    }


    public Set<String> keySet() {
        return sessionIdMap.keySet();
    }

    public void forEach(BiConsumer<? super String, ? super Session> action) {
        sessionIdMap.forEach(action);
    }

    public Set<Map.Entry<String, Session>> entrySet() {
        return sessionIdMap.entrySet();
    }

    public List<Session> toList() {
        return this.sessionIdMap.entrySet().stream().map(e -> e.getValue()).collect(Collectors.toList());
    }
}
