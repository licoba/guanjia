package com.example.dibage.accountb.dao;

import java.util.Map;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.AbstractDaoSession;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.identityscope.IdentityScopeType;
import org.greenrobot.greendao.internal.DaoConfig;

import com.example.dibage.accountb.entitys.Account;
import com.example.dibage.accountb.entitys.Card;
import com.example.dibage.accountb.entitys.Goods;
import com.example.dibage.accountb.entitys.Photo;

import com.example.dibage.accountb.dao.AccountDao;
import com.example.dibage.accountb.dao.CardDao;
import com.example.dibage.accountb.dao.GoodsDao;
import com.example.dibage.accountb.dao.PhotoDao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * {@inheritDoc}
 * 
 * @see org.greenrobot.greendao.AbstractDaoSession
 */
public class DaoSession extends AbstractDaoSession {

    private final DaoConfig accountDaoConfig;
    private final DaoConfig cardDaoConfig;
    private final DaoConfig goodsDaoConfig;
    private final DaoConfig photoDaoConfig;

    private final AccountDao accountDao;
    private final CardDao cardDao;
    private final GoodsDao goodsDao;
    private final PhotoDao photoDao;

    public DaoSession(Database db, IdentityScopeType type, Map<Class<? extends AbstractDao<?, ?>>, DaoConfig>
            daoConfigMap) {
        super(db);

        accountDaoConfig = daoConfigMap.get(AccountDao.class).clone();
        accountDaoConfig.initIdentityScope(type);

        cardDaoConfig = daoConfigMap.get(CardDao.class).clone();
        cardDaoConfig.initIdentityScope(type);

        goodsDaoConfig = daoConfigMap.get(GoodsDao.class).clone();
        goodsDaoConfig.initIdentityScope(type);

        photoDaoConfig = daoConfigMap.get(PhotoDao.class).clone();
        photoDaoConfig.initIdentityScope(type);

        accountDao = new AccountDao(accountDaoConfig, this);
        cardDao = new CardDao(cardDaoConfig, this);
        goodsDao = new GoodsDao(goodsDaoConfig, this);
        photoDao = new PhotoDao(photoDaoConfig, this);

        registerDao(Account.class, accountDao);
        registerDao(Card.class, cardDao);
        registerDao(Goods.class, goodsDao);
        registerDao(Photo.class, photoDao);
    }
    
    public void clear() {
        accountDaoConfig.clearIdentityScope();
        cardDaoConfig.clearIdentityScope();
        goodsDaoConfig.clearIdentityScope();
        photoDaoConfig.clearIdentityScope();
    }

    public AccountDao getAccountDao() {
        return accountDao;
    }

    public CardDao getCardDao() {
        return cardDao;
    }

    public GoodsDao getGoodsDao() {
        return goodsDao;
    }

    public PhotoDao getPhotoDao() {
        return photoDao;
    }

}
