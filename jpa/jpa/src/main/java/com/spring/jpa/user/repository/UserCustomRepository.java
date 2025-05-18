package com.spring.jpa.user.repository;

import com.spring.jpa.user.model.UserLogCount;
import com.spring.jpa.user.model.UserNoticeCount;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class UserCustomRepository {
    private final EntityManager em;


    public List<UserNoticeCount> findUserNoticeCount() {
        String sql = "select u.id, u.email, u.username, (select count(*) from Notice n where n.user_id = u.id) notice_count from User u";

        List<UserNoticeCount> list = em.createNativeQuery(sql).getResultList();
        return list;
    }

    public List<UserLogCount> findUserLogCount() {
        String sql = "select u.id, u.email, u.username, (select count(*) from notice n where n.user_id = u.id) notice_count " +
                ", (select count(*) from notice_like nl where nl.user_id = u.id) notice_count " +
                "from user u";

        List<UserLogCount> list = em.createNativeQuery(sql).getResultList();
        return list;
    }

    public List<UserLogCount> findUserLikeBest() {
        String sql = "select t1.id, t1.email, t1.username, t1.notice_like_count " +
                " from " +
                " ( " +
                " select u.*, (select count(*) from notice_like nl where nl.user_id = u.id) as notice_like_count " +
                " ) t1" +
                " order by t1.notice_like_count desc";

        List<UserLogCount> list = em.createQuery(sql).getResultList();

        return list;
    }
}
