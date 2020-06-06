package com.hengyijia.friend.service;

import com.hengyijia.friend.dao.FriendDao;
import com.hengyijia.friend.dao.NoFriendDao;
import com.hengyijia.friend.pojo.Friend;
import com.hengyijia.friend.pojo.NoFriend;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class FriendService {

    @Autowired
    private FriendDao friendDao;

    @Autowired
    private NoFriendDao noFriendDao;

    public int addFriend(String userid, String friendid) {
        //先判断userid到friendid是否有数据，有就是重复添加好友
            Friend result = friendDao.findByUseridAndFriendid(userid, friendid);
            if(result != null){
                //如果查到有数据，表示已经是好友
                return 0;
            }

        //添加好友，让好友表中userid到friendid方向的type为0  0是单向好友，1是双向好友
        Friend friend = new Friend();
            friend.setUserid(userid);
            friend.setFriendid(friendid);
            friend.setIslike("0");  //默认单向好友
            friendDao.save(friend);

        //判断从friendid到userid是否有数据，如果有，把双方的状态都改为1
        if(friendDao.findByUseridAndFriendid(friendid, userid) != null){
            //把从friendid到userid的islike改为1
            friendDao.updateIsLike("1", friendid, userid);
            //把从userid到friendid的islike改为1
            friendDao.updateIsLike("1", userid, friendid);
        }
        return 1;
    }

    public int addNoFriend(String userid, String friendid) {
        //先判断是否已经是非好友
        NoFriend result = noFriendDao.findByUseridAndFriendid(userid, friendid);
        if(result != null){
            //如果查到有数据，表示是非好友
            return 0;
        }
        NoFriend noFriend = new NoFriend();
        noFriend.setUserid(userid);
        noFriend.setFriendid(friendid);
        noFriendDao.save(noFriend);
        return 1;
    }

    public void deleteFriend(String userid, String friendid) {
        //删除好友表中userid到friendid这条数据
        friendDao.deleteFriend(userid, friendid);

        //更新friendid到userid的islike为0
        friendDao.updateIsLike("0", friendid, userid);

        //在非好友表中添加数据
        NoFriend noFriend = new NoFriend();
        noFriend.setUserid(userid);
        noFriend.setFriendid(friendid);
        noFriendDao.save(noFriend);
    }
}
