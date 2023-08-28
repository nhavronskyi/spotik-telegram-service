package com.example.spotiktelegramservice.dao.impl;

import com.example.spotiktelegramservice.dao.TelegramUserDao;
import com.example.spotiktelegramservice.entity.TelegramUser;
import lombok.RequiredArgsConstructor;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TelegramUserDaoImpl implements TelegramUserDao {
    private final SessionFactory sessionFactory;

    @Override
    public void save(TelegramUser user) {
        try (var em = sessionFactory.createEntityManager()) {
            em.getTransaction().begin();
            em.merge(user);
            em.getTransaction().commit();
        }
    }

    @Override
    public TelegramUser findById(String chatId) {
        try (var em = sessionFactory.createEntityManager()) {
            return em.find(TelegramUser.class, chatId);
        }
    }

    @Override
    public void activeStatus(String chatId, boolean status) {
        try (var em = sessionFactory.createEntityManager()) {
            em.getTransaction().begin();
            em.createQuery("UPDATE telegram_users SET active = (:status) WHERE id = (:id)")
                    .setParameter("status", status)
                    .setParameter("id", chatId)
                    .executeUpdate();
            em.getTransaction().commit();
        }
    }

    @Override
    public List<TelegramUser> getAllUsersWithActiveStatus() {
        try (var em = sessionFactory.createEntityManager()) {
            return em.createQuery("FROM telegram_users WHERE active = true", TelegramUser.class)
                    .getResultList();
        }
    }
}
