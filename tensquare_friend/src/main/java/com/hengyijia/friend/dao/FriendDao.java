package com.hengyijia.friend.dao;

import com.hengyijia.friend.pojo.Friend;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface FriendDao extends JpaRepository<Friend,String> {
    /**
     * 联合双主键查询
     * @param userid
     * @param friendid
     * @return
     */
    public Friend findByUseridAndFriendid(String userid, String friendid);

    @Modifying  //增删改要加Modifying
    @Query(value = "UPDATE tb_friend SET islike = ? WHERE userid = ? AND friendid = ?",nativeQuery = true)
    public void updateIsLike(String islike, String userid, String friendid);

    @Modifying  //增删改要加Modifying
    @Query(value = "DELETE FROM tb_friend WHERE userid = ? AND friendid = ?",nativeQuery = true)
    public void deleteFriend(String userid, String friendid);
}
