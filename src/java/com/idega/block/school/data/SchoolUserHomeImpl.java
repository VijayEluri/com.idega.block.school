package com.idega.block.school.data;

import java.rmi.RemoteException;


public class SchoolUserHomeImpl extends com.idega.data.IDOFactory implements SchoolUserHome
{
 protected Class getEntityInterfaceClass(){
  return SchoolUser.class;
 }


 public SchoolUser create() throws javax.ejb.CreateException{
  return (SchoolUser) super.createIDO();
 }


 public SchoolUser findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (SchoolUser) super.findByPrimaryKeyIDO(pk);
 }


public java.util.Collection findBySchoolAndType(com.idega.block.school.data.School p0,int p1)throws javax.ejb.FinderException, RemoteException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection theReturn = ((SchoolUserBMPBean)entity).ejbHomeFindBySchoolAndType(p0,p1);
	this.idoCheckInPooledEntity(entity);
	return theReturn;
}

public java.lang.Object getSchoolUserId(com.idega.block.school.data.School p0,com.idega.user.data.User p1,int p2)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.lang.Object theReturn = ((SchoolUserBMPBean)entity).ejbHomeGetSchoolUserId(p0,p1,p2);
	this.idoCheckInPooledEntity(entity);
	return theReturn;
}

public java.util.Collection findBySchoolAndUser(com.idega.block.school.data.School p0,com.idega.user.data.User p1)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection theReturn = ((SchoolUserBMPBean)entity).ejbHomeFindBySchoolAndUser(p0,p1);
	this.idoCheckInPooledEntity(entity);
	return theReturn;
}


}