package com.idega.block.school.business;

import java.rmi.RemoteException;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.Vector;

import javax.ejb.CreateException;
import javax.ejb.FinderException;
import javax.ejb.RemoveException;
import javax.transaction.UserTransaction;

import com.idega.block.school.SchoolConstants;
import com.idega.block.school.data.School;
import com.idega.block.school.data.SchoolArea;
import com.idega.block.school.data.SchoolAreaHome;
import com.idega.block.school.data.SchoolCategory;
import com.idega.block.school.data.SchoolCategoryBMPBean;
import com.idega.block.school.data.SchoolCategoryHome;
import com.idega.block.school.data.SchoolClass;
import com.idega.block.school.data.SchoolClassHome;
import com.idega.block.school.data.SchoolClassMember;
import com.idega.block.school.data.SchoolClassMemberHome;
import com.idega.block.school.data.SchoolClassMemberLog;
import com.idega.block.school.data.SchoolClassMemberLogHome;
import com.idega.block.school.data.SchoolDepartment;
import com.idega.block.school.data.SchoolDepartmentHome;
import com.idega.block.school.data.SchoolDistrict;
import com.idega.block.school.data.SchoolDistrictHome;
import com.idega.block.school.data.SchoolHome;
import com.idega.block.school.data.SchoolManagementType;
import com.idega.block.school.data.SchoolManagementTypeHome;
import com.idega.block.school.data.SchoolSeason;
import com.idega.block.school.data.SchoolSeasonHome;
import com.idega.block.school.data.SchoolStudyPath;
import com.idega.block.school.data.SchoolStudyPathHome;
import com.idega.block.school.data.SchoolSubArea;
import com.idega.block.school.data.SchoolSubAreaHome;
import com.idega.block.school.data.SchoolType;
import com.idega.block.school.data.SchoolTypeHome;
import com.idega.block.school.data.SchoolUser;
import com.idega.block.school.data.SchoolYear;
import com.idega.block.school.data.SchoolYearHome;
import com.idega.block.school.data.SchoolYearPlaces;
import com.idega.block.school.data.SchoolYearPlacesHome;
import com.idega.block.school.data.Student;
import com.idega.block.school.data.StudentHome;
import com.idega.block.text.data.LocalizedText;
import com.idega.block.text.data.TxText;
import com.idega.builder.bean.AdvancedProperty;
import com.idega.business.IBOLookup;
import com.idega.business.IBORuntimeException;
import com.idega.business.IBOServiceBean;
import com.idega.core.file.data.ICFile;
import com.idega.core.location.data.Commune;
import com.idega.core.location.data.CommuneHome;
import com.idega.data.IDOAddRelationshipException;
import com.idega.data.IDOException;
import com.idega.data.IDOLookup;
import com.idega.data.IDOLookupException;
import com.idega.data.IDORelationshipException;
import com.idega.data.IDORemoveRelationshipException;
import com.idega.user.business.GroupBusiness;
import com.idega.user.business.UserBusiness;
import com.idega.user.data.Group;
import com.idega.user.data.GroupHome;
import com.idega.user.data.User;
import com.idega.util.IWTimestamp;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company:
 * </p>
 *
 * @author <br>
 *         <a href="mailto:aron@idega.is">Aron Birkir </a> <br>
 * @version 1.0
 */
public class SchoolBusinessBean extends IBOServiceBean implements SchoolBusiness {

	private static final long serialVersionUID = 4330164695659907917L;

	public static final String GROUP_TYPE_SCHOOL_GROUP = "school_staff_group";

	private static SchoolCategory iSchoolCategoryAfterSchoolCare;

	private static SchoolCategory iSchoolCategoryChildCare;

	private static SchoolCategory iSchoolCategoryElementarySchool;

	private static SchoolCategory iSchoolCategoryHighSchool;

	private static SchoolCategory iSchoolCategoryCollege;

	private static SchoolCategory iSchoolCategoryUniversity;

	private static SchoolCategory iSchoolCategoryMusicSchool;

	private static SchoolCategory iSchoolCategoryAdultEducation;

	public final static String PROPERTY_NAME_REJECT_STUDENT_MESSAGE = "reject_student_message";

	public final static String PROPERTY_NAME_GROUP_OFFER_MESSAGE = "group_offer_body";

	public final static String PROPERTY_NAME_GROUP_CONFIRM_MESSAGE = "group_confirm_body";

	public final static String PROPERTY_NAME_USE_PLACEMENT_LOGGING = "log_placements";

	@Override
	public SchoolDepartmentHome getSchoolDepartmentHome() throws RemoteException {
		return (SchoolDepartmentHome) IDOLookup.getHome(SchoolDepartment.class);
	}

	@Override
	public SchoolHome getSchoolHome() {
		try {
			return (SchoolHome) IDOLookup.getHome(School.class);
		} catch (IDOLookupException e) {
			throw new IBORuntimeException(e.getMessage());
		}
	}

	@Override
	public SchoolClassMemberHome getSchoolClassMemberHome() {
		try {
			return (SchoolClassMemberHome) IDOLookup.getHome(SchoolClassMember.class);
		} catch (IDOLookupException e) {
			throw new IBORuntimeException(e.getMessage());
		}
	}

	@Override
	public SchoolClassHome getSchoolClassHome() {
		try {
			return (SchoolClassHome) IDOLookup.getHome(SchoolClass.class);
		} catch (IDOLookupException e) {
			throw new IBORuntimeException(e.getMessage());
		}
	}

	@Override
	public SchoolAreaHome getSchoolAreaHome() {
		try {
			return (SchoolAreaHome) IDOLookup.getHome(SchoolArea.class);
		} catch (IDOLookupException e) {
			throw new IBORuntimeException(e.getMessage());
		}
	}

	@Override
	public SchoolTypeHome getSchoolTypeHome() {
		try {
			return (SchoolTypeHome) IDOLookup.getHome(SchoolType.class);
		} catch (IDOLookupException e) {
			throw new IBORuntimeException(e.getMessage());
		}
	}

	@Override
	public SchoolCategoryHome getSchoolCategoryHome() {
		try {
			return (SchoolCategoryHome) IDOLookup.getHome(SchoolCategory.class);
		} catch (IDOLookupException e) {
			throw new IBORuntimeException(e.getMessage());
		}
	}

	@Override
	public SchoolSeasonHome getSchoolSeasonHome() {
		try {
			return (SchoolSeasonHome) IDOLookup.getHome(SchoolSeason.class);
		} catch (IDOLookupException e) {
			throw new IBORuntimeException(e.getMessage());
		}
	}

	@Override
	public SchoolStudyPathHome getSchoolStudyPathHome() {
		try {
			return (SchoolStudyPathHome) IDOLookup.getHome(SchoolStudyPath.class);
		} catch (IDOLookupException e) {
			throw new IBORuntimeException(e.getMessage());
		}
	}

	@Override
	public SchoolDistrictHome getSchoolDistrictHome() {
		try {
			return (SchoolDistrictHome) IDOLookup.getHome(SchoolDistrict.class);
		} catch (IDOLookupException e) {
			throw new IBORuntimeException(e.getMessage());
		}
	}

	@Override
	public SchoolManagementTypeHome getSchoolManagementTypeHome() {
		try {
			return (SchoolManagementTypeHome) IDOLookup.getHome(SchoolManagementType.class);
		} catch (IDOLookupException e) {
			throw new IBORuntimeException(e.getMessage());
		}
	}

	@Override
	public Student getStudent(User student) {
		try {
			if (student instanceof Student) {
				return (Student) student;
			}

			StudentHome home = (StudentHome) IDOLookup.getHome(Student.class);
			return home.findByPrimaryKey(student.getPrimaryKey());
		} catch (IDOLookupException e) {
			throw new IBORuntimeException(e.getMessage());
		} catch (FinderException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public Collection<SchoolDepartment> getDepartments(School school) throws RemoteException, FinderException {
		return getSchoolDepartmentHome().findAllDepartmentsBySchool(school);
	}

	@Override
	public int getDepartmentID(SchoolDepartment schDepm) {
		return schDepm.getDepartmentID();
	}

	@Override
	public void removeDepartment(SchoolDepartment schDep) throws RemoveException {
		schDep.remove();
	}

	@Override
	public void addSchoolUsr(int schDep_id, SchoolUser schUser) throws FinderException, RemoteException {
		try {
			SchoolDepartmentHome schDepHome = (SchoolDepartmentHome) IDOLookup.getHome(SchoolDepartment.class);
			SchoolDepartment schDep = schDepHome.findByPrimaryKey(new Integer(schDep_id));
			schDep.addSchoolUser(schUser);
		} catch (IDOAddRelationshipException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void removeSchoolUsr(int schDep_id, SchoolUser schUser) throws FinderException, RemoteException, IDORemoveRelationshipException {
		SchoolDepartmentHome schDepHome = (SchoolDepartmentHome) IDOLookup.getHome(SchoolDepartment.class);
		SchoolDepartment schDep = schDepHome.findByPrimaryKey(new Integer(schDep_id));
		schDep.removeSchoolUser(schUser);
	}

	@Override
	public Collection<SchoolCategory> getSchoolCategories() {
		try {
			return getSchoolCategoryHome().findAllCategories();
		} catch (FinderException e) {
			return null;
		}
	}

	@Override
	public Collection<SchoolCategory> getSchoolCategories(School school) {
		Collection<SchoolCategory> categories = new ArrayList<SchoolCategory>();

		try {
			Collection<SchoolType> types = school.getSchoolTypes();
			for (Iterator<SchoolType> iterator = types.iterator(); iterator.hasNext();) {
				SchoolType type = iterator.next();
				SchoolCategory category = type.getCategory();

				if (!categories.contains(category)) {
					categories.add(category);
				}
			}
		} catch (IDORelationshipException e) {
			e.printStackTrace();
		}

		return categories;
	}

	@Override
	public SchoolCategory getCategoryMusicSchool() {
		if (iSchoolCategoryMusicSchool == null) {
			try {
				iSchoolCategoryMusicSchool = getSchoolCategoryHome().findMusicSchoolCategory();
			} catch (FinderException e) {
				try {
					iSchoolCategoryMusicSchool = getSchoolCategoryHome().create();
					iSchoolCategoryMusicSchool.setCategory(SchoolCategoryBMPBean.CATEGORY_MUSIC_SCHOOL);
					iSchoolCategoryMusicSchool.setName("Music school");
					iSchoolCategoryMusicSchool.setLocalizedKey("school_category." + SchoolCategoryBMPBean.CATEGORY_MUSIC_SCHOOL);
					iSchoolCategoryMusicSchool.store();
				} catch (CreateException ce) {
					log(ce);
				}
			}
		}
		return iSchoolCategoryMusicSchool;
	}

	@Override
	public SchoolCategory getCategoryAfterSchoolCare() {
		if (iSchoolCategoryAfterSchoolCare == null) {
			try {
				iSchoolCategoryAfterSchoolCare = getSchoolCategoryHome().findAfterSchoolCareCategory();
			} catch (FinderException e) {
				iSchoolCategoryAfterSchoolCare = null;
			}
		}
		return iSchoolCategoryAfterSchoolCare;
	}

	@Override
	public SchoolCategory getCategoryChildcare() {
		if (iSchoolCategoryChildCare == null) {
			try {
				iSchoolCategoryChildCare = getSchoolCategoryHome().findChildcareCategory();
			} catch (FinderException e) {
				iSchoolCategoryChildCare = null;
			}
		}
		return iSchoolCategoryChildCare;
	}

	@Override
	public SchoolCategory getCategoryElementarySchool() {
		if (iSchoolCategoryElementarySchool == null) {
			try {
				iSchoolCategoryElementarySchool = getSchoolCategoryHome().findElementarySchoolCategory();
			} catch (FinderException e) {
				iSchoolCategoryElementarySchool = null;
			}
		}
		return iSchoolCategoryElementarySchool;
	}

	@Override
	public SchoolCategory getCategoryHighSchool() {
		if (iSchoolCategoryHighSchool == null) {
			try {
				iSchoolCategoryHighSchool = getSchoolCategoryHome().findHighSchoolCategory();
			} catch (FinderException e) {
				iSchoolCategoryHighSchool = null;
			}
		}
		return iSchoolCategoryHighSchool;
	}

	@Override
	public SchoolCategory getCategoryCollege() {
		if (iSchoolCategoryCollege == null) {
			try {
				iSchoolCategoryCollege = getSchoolCategoryHome().findCollegeCategory();
			} catch (FinderException e) {
				iSchoolCategoryCollege = null;
			}
		}
		return iSchoolCategoryCollege;
	}

	@Override
	public SchoolCategory getCategoryUniversity() {
		if (iSchoolCategoryUniversity == null) {
			try {
				iSchoolCategoryUniversity = getSchoolCategoryHome().findUniversityCategory();
			} catch (FinderException e) {
				iSchoolCategoryUniversity = null;
			}
		}
		return iSchoolCategoryUniversity;
	}

	@Override
	public SchoolCategory getCategoryAdultEducation() {
		if (iSchoolCategoryAdultEducation == null) {
			try {
				iSchoolCategoryAdultEducation = getSchoolCategoryHome().findAdultEducationCategory();
			} catch (FinderException e) {
				iSchoolCategoryAdultEducation = null;
			}
		}
		return iSchoolCategoryAdultEducation;
	}

	@Override
	public Collection<SchoolManagementType> getSchoolManagementTypes() {
		try {
			return getSchoolManagementTypeHome().findAllManagementTypes();
		} catch (FinderException e) {
			return null;
		}
	}

	@Override
	public School getSchool(Object primaryKey) {
		try {
			return getSchoolHome().findByPrimaryKey(primaryKey);
		} catch (FinderException fe) {
			return null;
		}
	}

	@Override
	public SchoolStudyPath getSchoolStudyPath(Object primaryKey) {
		try {
			return getSchoolStudyPathHome().findByPrimaryKey(primaryKey);
		} catch (FinderException fe) {
			return null;
		}
	}

	@Override
	public void removeProvider(int id) throws RemoveException {
		removeProvider(new Integer(id));
	}

	@Override
	public void removeProvider(Object primaryKey) throws RemoveException {
		School school = getSchool(primaryKey);
		school.addSchoolTypesRemoveOther(new int[0]);
		school.addSchoolYearsRemoveOther(new int[0]);
		try {
			school.removeFromClass(ICFile.class);
		} catch (IDORemoveRelationshipException e) {
			System.err.println("Cannot remove file from school");
		}
		try {
			school.removeFromClass(TxText.class);
		} catch (IDORemoveRelationshipException e) {
			System.err.println("Cannot remove text from school");
		}
		try {
			school.removeFromClass(LocalizedText.class);
		} catch (IDORemoveRelationshipException e) {
			System.err.println("Cannot remove text from school");
		}
		try {
			Collection<SchoolUser> coll = getSchoolUserBusiness().getSchoolUserHome().findBySchool(school);
			SchoolUser sUser;
			if (coll != null && !coll.isEmpty()) {
				Iterator<SchoolUser> iter = coll.iterator();
				while (iter.hasNext()) {
					sUser = getSchoolUserBusiness().getSchoolUserHome().findByPrimaryKey(iter.next());
					sUser.remove();
				}
			}
		} catch (Exception e) {
			e.printStackTrace(System.err);
		}
		school.remove();
	}

	@Override
	public void removeSchool(int id) {
		try {
			removeProvider(id);
		} catch (RemoveException re) {
			re.printStackTrace();
		}
	}

	@Override
	public Collection<School> findAllSchools() {
		try {
			return getSchoolHome().findAllSchools();
		} catch (FinderException fe) {
			fe.printStackTrace();
			return Collections.emptyList();
		}
	}

	@Override
	public Collection findAllSchoolsByAreaAndType(int area, int type) {
		try {
			return getSchoolHome().findAllByAreaAndType(area, type);
		} catch (FinderException fe) {
			fe.printStackTrace();
			return new Vector();
		}
	}

	@Override
	public Collection findAllSchoolsByAreaAndTypeAndYear(int areaID, int typeID, int yearID) {
		try {
			return getSchoolHome().findAllByAreaAndTypeAndYear(areaID, typeID, yearID);
		} catch (FinderException fe) {
			fe.printStackTrace();
			return new Vector();
		}
	}

	@Override
	public Collection findAllSchoolsByAreaAndTypes(int area, Collection types) {
		try {
			return getSchoolHome().findAllByAreaAndTypes(area, types);
		} catch (FinderException fe) {
			fe.printStackTrace();
			return new Vector();
		}
	}

	@Override
	public Collection findAllSchoolsBySubAreaAndTypes(int subarea, Collection types) {
		try {
			return getSchoolHome().findAllBySubAreaAndTypes(subarea, types);
		} catch (FinderException fe) {
			fe.printStackTrace();
			return new Vector();
		}
	}

	@Override
	public java.util.Collection findAllCentralizedAdministrated() {
		try {
			return getSchoolHome().findAllCentralizedAdministrated();
		} catch (FinderException fe) {
			fe.printStackTrace();
			return new java.util.HashSet();
		}
	}

	@Override
	public java.util.Collection findAllCentralizedAdministratedByType(Collection typeIds) {
		try {
			return getSchoolHome().findAllCentralizedAdministratedByType(typeIds);
		} catch (FinderException fe) {
			fe.printStackTrace();
			return new java.util.HashSet();
		}
	}

	@Override
	public School createSchool(String name, String address, String zipcode, String ziparea, String phone, int school_type, Object communePK) throws RemoteException {
		/**
		 * @todo figure out how to implement
		 */
		int area_id = -1;
		int sch_types[] = { school_type };
		return createSchool(name, null, address, zipcode, ziparea, phone, null, null, null, area_id, sch_types, communePK);
	}

	@Override
	public School createSchool(String name, String address, String zipcode, String ziparea, String phone, int area_id, int[] sch_types, Object communePK) throws RemoteException {
		return createSchool(name, null, address, zipcode, ziparea, phone, null, null, null, area_id, sch_types, communePK);
	}

	@Override
	public School createSchool(String name, String info, String address, String zipcode, String ziparea, String phone, String keycode, String latitude, String longitude, int area_id, int[] type_ids, Object communePK) throws RemoteException {
		return createSchool(name, info, address, zipcode, ziparea, phone, keycode, latitude, longitude, area_id, type_ids, null, communePK);
	}

	@Override
	public School createSchool(String name, String info, String address, String zipcode, String ziparea, String phone, String keycode, String latitude, String longitude, int area_id, int[] type_ids, int[] year_ids, Object communePK) throws RemoteException {
		return storeSchool(-1, name, info, address, zipcode, ziparea, phone, keycode, latitude, longitude, area_id, type_ids, year_ids, communePK);
	}

	@Override
	public School storeSchool(int id, String name, String info, String address, String zipcode, String ziparea, String phone, String keycode, String latitude, String longitude, int area_id, int[] type_ids, int[] year_ids, Object communePK) throws RemoteException {
		return storeSchool(id, name, info, address, zipcode, ziparea, phone, keycode, latitude, longitude, area_id, -1, type_ids, year_ids, null, null, null, null, communePK, -1, null, null);
	}

	@Override
	public School storeSchool(int id, String name, String info, String address, String zipcode, String ziparea, String phone, String keycode, String latitude, String longitude, int area_id, int[] type_ids, int[] year_ids, Object communePK, String providerStringId) throws RemoteException {
		return storeSchool(id, name, info, address, zipcode, ziparea, phone, keycode, latitude, longitude, area_id, -1, type_ids, year_ids, null, null, null, null, communePK, -1, null, null, providerStringId);
	}

	@Override
	public School storeSchool(int id, String name, String info, String address, String zipcode, String ziparea, String phone, String keycode, String latitude, String longitude, int area_id, int sub_area_id, int[] type_ids, int[] year_ids, String organizationNumber, String extraProviderId, String managementTypeId, java.sql.Date terminationDate, Object communePK, int countryId, Boolean centralizedAdministration, Boolean invisibleForCitizen) throws RemoteException {
		return storeSchool(id, name, info, address, zipcode, ziparea, phone, keycode, latitude, longitude, area_id, sub_area_id, type_ids, year_ids, organizationNumber, extraProviderId, managementTypeId, terminationDate, communePK, countryId, centralizedAdministration, invisibleForCitizen, null);
	}

	@Override
	public School storeSchool(int id, String name, String info, String address, String zipcode, String ziparea, String phone, String keycode, String latitude, String longitude, int area_id, int sub_area_id, int[] type_ids, int[] year_ids, String organizationNumber, String extraProviderId, String managementTypeId, java.sql.Date terminationDate, Object communePK, int countryId, Boolean centralizedAdministration, Boolean invisibleForCitizen, String providerStringId) throws RemoteException {
		return storeSchool(id, name, info, address, zipcode, ziparea, phone, keycode, latitude, longitude, area_id, sub_area_id, type_ids, year_ids, organizationNumber, extraProviderId, managementTypeId, terminationDate, communePK, countryId, centralizedAdministration, invisibleForCitizen, providerStringId, null);
	}

	@Override
	public School storeSchool(int id, String name, String info, String address, String zipcode, String ziparea, String phone, String keycode, String latitude, String longitude, int area_id, int sub_area_id, int[] type_ids, int[] year_ids, String organizationNumber, String extraProviderId, String managementTypeId, java.sql.Date terminationDate, Object communePK, int countryId, Boolean centralizedAdministration, Boolean invisibleForCitizen, String providerStringId, Boolean sortByBirthdate) throws RemoteException {
		SchoolHome shome = getSchoolHome();
		School newSchool;
		try {
			if (id > -1) {
				newSchool = shome.findByPrimaryKey(new Integer(id));
			} else {
				newSchool = shome.create();
			}
			if (newSchool.getHeadmasterGroupId() < 0) {
				newSchool.setHeadmasterGroupId(((Integer) getNewSchoolGroup(name, name).getPrimaryKey()).intValue());
			}
		} catch (javax.ejb.FinderException fe) {
			throw new java.rmi.RemoteException(fe.getMessage());
		} catch (javax.ejb.CreateException ce) {
			throw new java.rmi.RemoteException(ce.getMessage());
		}
		if (area_id > 0) {
			newSchool.setSchoolAreaId(area_id);
		}
		if (sub_area_id > 0) {
			newSchool.setSchoolSubAreaId(sub_area_id);
		}
		if (address != null) {
			newSchool.setSchoolAddress(address);
		}
		if (info != null) {
			newSchool.setSchoolInfo(info);
		}
		if (keycode != null) {
			newSchool.setSchoolKeyCode(keycode);
		}
		if (latitude != null) {
			newSchool.setSchoolLatitude(latitude);
		}
		if (longitude != null) {
			newSchool.setSchoolLongitude(longitude);
		}
		if (name != null) {
			newSchool.setSchoolName(name);
		}
		if (phone != null) {
			newSchool.setSchoolPhone(phone);
		}
		if (ziparea != null) {
			newSchool.setSchoolZipArea(ziparea);
		}
		if (zipcode != null) {
			newSchool.setSchoolZipCode(zipcode);
		}
		if (organizationNumber != null) {
			newSchool.setOrganizationNumber(organizationNumber);
		}
		if (extraProviderId != null) {
			newSchool.setExtraProviderId(extraProviderId);
		}
		if (managementTypeId != null) {
			newSchool.setManagementTypeId(managementTypeId);
		}
		if (terminationDate != null) {
			newSchool.setTerminationDate(terminationDate);
		}
		if (communePK != null) {
			newSchool.setCommunePK(communePK);
		}
		if (countryId > 0) {
			newSchool.setCountryId(countryId);
		}
		if (centralizedAdministration != null) {
			newSchool.setCentralizedAdministration(centralizedAdministration.booleanValue());
		}
		if (invisibleForCitizen != null) {
			newSchool.setInvisibleForCitizen(invisibleForCitizen.booleanValue());
		}
		if (providerStringId != null) {
			newSchool.setProviderStringId(providerStringId);
		}
		if (sortByBirthdate != null) {
			newSchool.setSortByBirthdate(sortByBirthdate.booleanValue());
		}

		newSchool.store();
		if (type_ids != null) {
			newSchool.addSchoolTypesRemoveOther(type_ids);
		}
		if (year_ids != null) {
			newSchool.addSchoolYearsRemoveOther(year_ids);
		}
		return newSchool;
	}

	@Override
	public Map getSchoolRelatedSchoolTypes(School school) {
		try {
			Collection types = school.findRelatedSchoolTypes();
			if (types != null && !types.isEmpty()) {
				HashMap map = new HashMap(types.size());
				Iterator iter = types.iterator();
				SchoolType type;
				while (iter.hasNext()) {
					type = (SchoolType) iter.next();
					map.put(type.getPrimaryKey(), type);
				}
				return map;
			}
		} catch (IDORelationshipException ie) {
			ie.printStackTrace();
		}
		return null;
	}

	@Override
	public Map getSchoolRelatedSchoolYears(School school) {
		try {
			Collection years = school.findRelatedSchoolYears();
			if (years != null && !years.isEmpty()) {
				HashMap map = new HashMap(years.size());
				Iterator iter = years.iterator();
				SchoolYear year;
				while (iter.hasNext()) {
					year = (SchoolYear) iter.next();
					map.put(year.getPrimaryKey(), year);
				}
				return map;
			}
		} catch (IDORelationshipException ie) {
			ie.printStackTrace();
		}
		return null;
	}

	@Override
	public Map getSchoolAndSchoolTypeRelatedSchoolCourses(School school, Object schoolTypeId) {
		try {
			SchoolStudyPathHome scHome = (SchoolStudyPathHome) IDOLookup.getHome(SchoolStudyPath.class);
			Collection courses = scHome.findStudyPaths(school, schoolTypeId);
			if (courses != null && !courses.isEmpty()) {
				HashMap map = new HashMap(courses.size());
				Iterator iter = courses.iterator();
				SchoolStudyPath course;
				while (iter.hasNext()) {
					course = scHome.findByPrimaryKey(iter.next());
					map.put(course.getPrimaryKey(), course);
				}
				return map;
			}
		} catch (FinderException e) {
			e.printStackTrace();
		} catch (IDOLookupException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public Map getMapOfSchools() {
		try {
			Collection schools = findAllSchools();
			if (schools != null && !schools.isEmpty()) {
				HashMap map = new HashMap(schools.size());
				Iterator iter = schools.iterator();
				School school;
				while (iter.hasNext()) {
					school = (School) iter.next();
					map.put(school.getPrimaryKey(), school);
				}
				return map;
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}

	@Override
	public Group getNewSchoolGroup(String name, String info) throws RemoteException {
		try {
			Group rootGroup = this.getRootSchoolGroup();
			Group schoolGroup = getUserBusiness().getGroupBusiness().createGroupUnder(name, info, rootGroup);
			return schoolGroup;
		} catch (CreateException ce) {
			ce.printStackTrace(System.err);
			return null;
		}
	}

	private Group getRootSchoolGroup() throws RemoteException {
		try {
			String ROOT_SCHOOL_GROUP_ID_PARAMETER = "root_school_group";
			Group rootGroup = null;

			String groupId = getPropertyValue(ROOT_SCHOOL_GROUP_ID_PARAMETER);
			if (groupId != null) {
				rootGroup = getUserBusiness().getGroupHome().findByPrimaryKey(new Integer(groupId));
			} else {
				try {
					System.err.println("trying to store School Root group");
					rootGroup = getUserBusiness().getGroupBusiness().createGroup("School Root Group", "The School Root Group.");
					setProperty(ROOT_SCHOOL_GROUP_ID_PARAMETER, rootGroup.getPrimaryKey().toString());
				} catch (CreateException ce) {
					ce.printStackTrace(System.err);
				}
			}
			return rootGroup;
		} catch (FinderException fe) {
			fe.printStackTrace(System.err);
			return null;
		}
	}

	@Override
	public Group getRootSchoolCategoryGroup(SchoolCategory category) {
		try {
			String ROOT_SCHOOL_CATEGORY_GROUP_ID_PARAMETER = "category." + category.getCategory().toLowerCase();
			Group rootGroup = null;

			String groupId = getPropertyValue(ROOT_SCHOOL_CATEGORY_GROUP_ID_PARAMETER);
			if (groupId != null) {
				rootGroup = getUserBusiness().getGroupHome().findByPrimaryKey(new Integer(groupId));
			} else {
				try {
					System.err.println("trying to store " + category.getCategory() + " group");

					Group oldGroup = getRootGroup(category);
					if (oldGroup != null) {
						rootGroup = getUserBusiness().getGroupBusiness().createGroupUnder(category.getCategory().toLowerCase(), category.getCategory().toLowerCase(), SchoolConstants.GROUP_TYPE_SCHOOL_CATEGORY, oldGroup.getHomePageID(), -1, getRootSchoolGroup());
					} else {
						rootGroup = getUserBusiness().getGroupBusiness().createGroupUnder(category.getCategory().toLowerCase(), category.getCategory().toLowerCase(), SchoolConstants.GROUP_TYPE_SCHOOL_CATEGORY, getRootSchoolGroup());
					}
					setProperty(ROOT_SCHOOL_CATEGORY_GROUP_ID_PARAMETER, rootGroup.getPrimaryKey().toString());
				} catch (CreateException ce) {
					ce.printStackTrace(System.err);
				}
			}
			return rootGroup;
		} catch (FinderException fe) {
			fe.printStackTrace(System.err);
			return null;
		} catch (RemoteException re) {
			throw new IBORuntimeException(re);
		} catch (NullPointerException e) {
			throw new IBORuntimeException(e);
		}
	}

	@Override
	public boolean hasEditPermission(User user, School school) throws RemoteException {
		UserBusiness uBus = IBOLookup.getServiceInstance(getIWApplicationContext(), UserBusiness.class);
		if (user != null && school != null) {
			Collection users = null;
			try {
				users = uBus.getGroupBusiness().getUsers(school.getHeadmasterGroupId());
			} catch (FinderException e) {
				e.printStackTrace(System.err);
			}
			if (users != null) {
				Iterator iter = users.iterator();
				while (iter.hasNext()) {
					if (iter.next().equals(user)) {
						return true;
					}
				}
			}
		}
		return false;
	}

	@Override
	public Collection getSchoolGroups(User user) throws RemoteException {
		Collection userSchoolGroups = getUserBusiness().getUserGroups(user, new String[] { GROUP_TYPE_SCHOOL_GROUP }, true);
		if (userSchoolGroups != null) {
			// System.out.println("SchooBusiness.getSchoolGroup : found
			// schoolGroups...");
			return userSchoolGroups;
		} else {
			// System.out.println("SchooBusiness.getSchoolGroup : schoolGroups
			// NOT
			// FOUND...");
			/** backwards compatabilit, ... finna betri leid ?? */
			Collection userGroups = getUserBusiness().getUserGroups(user);
			Collection schools = this.findAllSchools();
			if (schools != null && userGroups != null) {
				// System.out.println("SchooBusiness.getSchoolGroup : schools !=
				// null &&
				// userGroups != null...");
				Collection returner = new Vector();
				School school;
				Group group;
				GroupHome gHome = (GroupHome) IDOLookup.getHome(Group.class);
				Iterator itSchools = schools.iterator();
				Iterator itGroups = userGroups.iterator();
				while (itSchools.hasNext()) {
					try {
						school = getSchoolHome().findByPrimaryKey(itSchools.next());
						while (itGroups.hasNext()) {
							group = gHome.findByPrimaryKey(itGroups.next());
							if (group.getName().equals(school.getName())) {
								// System.out.println("SchooBusiness.getSchoolGroup
								// :
								// group.getName().equals(school.getName())
								// ...");
								group.setGroupType(GROUP_TYPE_SCHOOL_GROUP);
								group.store();
								returner.add(group.getPrimaryKey());
							}
						}
					} catch (FinderException e) {
						e.printStackTrace(System.err);
					}
				}
				return returner;
			}
		}
		return null;
	}

	protected UserBusiness getUserBusiness() {
		try {
			return this.getServiceInstance(UserBusiness.class);
		} catch (RemoteException e) {
			throw new IBORuntimeException(e);
		}
	}

	@Override
	public SchoolClass storeSchoolClass(String schoolClassName, School school, SchoolYear year, SchoolSeason season) {
		if (year != null && season != null) {
			return storeSchoolClass(-1, schoolClassName, ((Integer) school.getPrimaryKey()).intValue(), ((Integer) season.getPrimaryKey()).intValue(), ((Integer) year.getPrimaryKey()).intValue(), -1);
		} else {
			return storeSchoolClass(-1, schoolClassName, ((Integer) school.getPrimaryKey()).intValue(), -1, -1, -1);
		}
	}

	@Override
	public SchoolClassMember storeSchoolClassMember(SchoolClass sClass, User user) {
		return storeSchoolClassMember(((Integer) user.getPrimaryKey()).intValue(), ((Integer) sClass.getPrimaryKey()).intValue(), -1, -1, IWTimestamp.getTimestampRightNow(), -1);
		/*
		 * try { SchoolClassMemberHome sClassMemberHome =
		 * (SchoolClassMemberHome) this.getIDOHome(SchoolClassMember.class);
		 * SchoolClassMember sClassMember = sClassMemberHome.create();
		 * sClassMember.setSchoolClassId(((Integer)
		 * sClass.getPrimaryKey()).intValue());
		 * sClassMember.setClassMemberId(((Integer)
		 * user.getPrimaryKey()).intValue());
		 * sClassMember.setRegisterDate(IWTimestamp.getTimestampRightNow());
		 * sClassMember
		 * .setRegistrationCreatedDate(IWTimestamp.getTimestampRightNow());
		 * //NEEDS THE CURRENT USER ID FOR REGISTERING USER return sClassMember;
		 * } catch (CreateException ce) { ce.printStackTrace(System.err); return
		 * null; }
		 */
	}

	/**
	 * Gets the allowed values of the column invoice_int of table
	 * sch_class_member, as a Collection, from String constants in
	 * SchoolClassMemberBMPBean.
	 *
	 * @return Collection of type values
	 * @throws RemoteException
	 * @author Borgman
	 */

	@Override
	public Collection findAllSchClMemberInvoiceIntervalTypes() {
		return getSchoolClassMemberHome().getInvoiceIntervalTypes();
	}

	/**
	 * getAssistantHeadmasters added by Kelly (kelly@lindman.se) 15 may 2003
	 */

	@Override
	public Collection getAssistantHeadmasters(School school) throws RemoteException {
		try {
			return getUserBusiness().getGroupBusiness().getUsers(school.getAssistantHeadmasterGroupId());
		} catch (FinderException fe) {
			throw new RemoteException(fe.getMessage());
		}
	}

	@Override
	public Collection getSchoolUsers(School school) throws RemoteException {
		try {
			return getSchoolUserBusiness().getSchoolUserHome().findBySchool(school);
		} catch (FinderException fe) {
			throw new RemoteException(fe.getMessage());
		}
	}

	@Override
	public Collection getAllSchoolUsers(School school) throws RemoteException {
		Set users = new TreeSet();

		try {
			users.addAll(getSchoolUserBusiness().getSchoolUserHome().findBySchool(school));
		} catch (FinderException fe) {
			fe.printStackTrace();
		}

		try {
			users.addAll(getSchoolUserBusiness().getSchoolUserHome().findRelatedToSchool(school));
		} catch (FinderException fe) {
			fe.printStackTrace();
		}

		return users;
	}

	@Override
	public Collection getHeadmasters(School school) throws RemoteException {
		try {
			return getUserBusiness().getGroupBusiness().getUsers(school.getHeadmasterGroupId());
		} catch (FinderException fe) {
			throw new RemoteException(fe.getMessage());
		}
	}

	@Override
	public User getHeadmaster(int schoolID) throws RemoteException {
		try {
			School school = getSchoolHome().findByPrimaryKey(new Integer(schoolID));
			return getUserBusiness().getUser(school.getHeadmasterUserId());
		} catch (FinderException fe) {
			return null;
		}
	}

	/**
	 * Used for user who have admin rights, not only headmasters...
	 */

	@Override
	public void addHeadmaster(School school, User user) throws RemoteException, FinderException {
		GroupBusiness gBus = IBOLookup.getServiceInstance(getIWApplicationContext(), GroupBusiness.class);
		if (school.getHeadmasterGroupId() == -1) {
			school.setHeadmasterGroupId(((Integer) getNewSchoolGroup(school.getSchoolName(), school.getSchoolName()).getPrimaryKey()).intValue());
		}
		Group schoolGroup = gBus.getGroupHome().findByPrimaryKey(new Integer(school.getHeadmasterGroupId()));
		schoolGroup.addGroup(user);
		// getUserBusiness().getGroupBusiness().addUser(school.getHeadmasterGroupId(),
		// user);
	}

	@Override
	public Collection findAllSchoolsByType(Collection typeIds) {
		try {
			SchoolHome shome = getSchoolHome();
			return shome.findAllBySchoolType(typeIds);
		} catch (Exception ex) {
			ex.printStackTrace();
			return new java.util.Vector();
		}
	}

	@Override
	public Collection findAllGroupsBySchoolDWR(String school) {
		Collection result = new ArrayList();
		if (school == null || school.length() == 0 || school.equals("-1")) {
			return result;
		}
		result.add(new AdvancedProperty("-1", "Select"));
		try {
			Collection groups = getSchoolClassHome().findBySchool(Integer.parseInt(school));
			if (groups.isEmpty()) {
				result.add(new AdvancedProperty("-1", "Unavailable"));
				return result;
			}
			Iterator iter = groups.iterator();
			while (iter.hasNext()) {
				SchoolClass group = (SchoolClass) iter.next();
				result.add(new AdvancedProperty(group.getPrimaryKey().toString(), group.getName()));
			}
		} catch (FinderException ex) {
			ex.printStackTrace();
		}
		return result;
	}

	@Override
	public Collection findAllSchoolsByTypeDWR(String type) {
		Collection result = new ArrayList();
		if (type == null || type.length() == 0 || type.equals("-1")) {
			return result;
		}
		result.add(new AdvancedProperty("-1", "Select"));
		try {
			SchoolHome shome = getSchoolHome();
			Collection schools = shome.findAllBySchoolType(Integer.parseInt(type));
			if (schools.isEmpty()) {
				result.add(new AdvancedProperty("-1", "Unavailable"));
				return result;
			}
			Iterator iter = schools.iterator();
			while (iter.hasNext()) {
				School school = (School) iter.next();
				result.add(new AdvancedProperty(school.getPrimaryKey().toString(), school.getSchoolName()));
			}
		} catch (FinderException ex) {
			ex.printStackTrace();
		}
		return result;
	}

	@Override
	public Collection findAllSchoolsByType(int type) {
		try {
			SchoolHome shome = getSchoolHome();
			return shome.findAllBySchoolType(type);
		} catch (FinderException ex) {
			ex.printStackTrace();
			return new java.util.Vector();
		}
	}

	@Override
	public Collection findAllSchoolsByType(SchoolType type) {
		try {
			SchoolHome shome = getSchoolHome();
			return shome.findAllBySchoolType(type);
		} catch (FinderException ex) {
			ex.printStackTrace();
			return new java.util.Vector();
		}
	}

	@Override
	public Collection findAllSchoolYearsInSchool(int schoolID) {
		try {
			School school = this.getSchool(new Integer(schoolID));
			return school.findRelatedSchoolYears();
		} catch (IDORelationshipException ie) {
			return new Vector();
		}
	}

	/**
	 * Returns or creates (if not available) the default usergroup all school
	 * administors have as their primary group.
	 *
	 * @throws CreateException
	 *             if it failed to create the group.
	 * @throws FinderException
	 *             if it failed to locate the group.
	 */

	@Override
	public Group getRootSchoolAdministratorGroup() throws CreateException, FinderException, RemoteException {
		Group rootGroup = null;
		// create the default group
		String ROOT_SCHOOL_ADMINISTRATORS_GROUP = "school_administrators_group_id";
		String groupId = getPropertyValue(ROOT_SCHOOL_ADMINISTRATORS_GROUP);
		if (groupId != null) {
			rootGroup = getUserBusiness().getGroupHome().findByPrimaryKey(new Integer(groupId));
		} else {
			System.err.println("trying to store Commune Root school administrators group");
			rootGroup = getUserBusiness().getGroupBusiness().createGroup("School Administrators", "The Commune Root School Administrators Group.");
			setProperty(ROOT_SCHOOL_ADMINISTRATORS_GROUP, rootGroup.getPrimaryKey().toString());
		}
		return rootGroup;
	}

	/**
	 * Returns or creates (if not available) the default usergroup all high
	 * school administors have as their primary group.
	 *
	 * @throws CreateException
	 *             if it failed to create the group.
	 * @throws FinderException
	 *             if it failed to locate the group.
	 */

	@Override
	public Group getRootHighSchoolAdministratorGroup() throws CreateException, FinderException, RemoteException {
		Group rootGroup = null;

		// create the default group
		String ROOT_HIGH_SCHOOL_ADMINISTRATORS_GROUP = "high_school_administrators_group_id";
		String groupId = getPropertyValue(ROOT_HIGH_SCHOOL_ADMINISTRATORS_GROUP);
		if (groupId != null) {
			rootGroup = getUserBusiness().getGroupHome().findByPrimaryKey(new Integer(groupId));
		} else {
			System.err.println("trying to store Commune Root high school administrators group");
			rootGroup = getUserBusiness().getGroupBusiness().createGroup("High School Administrators", "The Commune Root High School Administrators Group.");
			setProperty(ROOT_HIGH_SCHOOL_ADMINISTRATORS_GROUP, rootGroup.getPrimaryKey().toString());
		}
		return rootGroup;
	}

	/**
	 * Returns or creates (if not available) the default usergroup all school
	 * administors have as their primary group.
	 *
	 * @throws CreateException
	 *             if it failed to create the group.
	 * @throws FinderException
	 *             if it failed to locate the group.
	 */

	@Override
	public Group getRootMusicSchoolAdministratorGroup() throws CreateException, FinderException, RemoteException {
		Group rootGroup = null;
		// create the default group
		String ROOT_MUSIC_SCHOOL_ADMINISTRATORS_GROUP = "music_administrators_group_id";
		String groupId = getPropertyValue(ROOT_MUSIC_SCHOOL_ADMINISTRATORS_GROUP);
		if (groupId != null) {
			rootGroup = getUserBusiness().getGroupHome().findByPrimaryKey(new Integer(groupId));
		} else {
			System.err.println("trying to store Commune Root school administrators group");
			rootGroup = getUserBusiness().getGroupBusiness().createGroup("Music School Administrators", "The Commune Root Music School Administrators Group.");
			setProperty(ROOT_MUSIC_SCHOOL_ADMINISTRATORS_GROUP, rootGroup.getPrimaryKey().toString());
		}
		return rootGroup;
	}

	/**
	 * Returns or creates (if not available) the default usergroup all
	 * provider(childcare) administors have as their primary group.
	 *
	 * @throws CreateException
	 *             if it failed to create the group.
	 * @throws FinderException
	 *             if it failed to locate the group.
	 */

	@Override
	public Group getRootProviderAdministratorGroup() throws CreateException, FinderException, RemoteException {
		Group rootGroup = null;
		// create the default group
		String ROOT_SCHOOL_ADMINISTRATORS_GROUP = "provider_administrators_group_id";
		String groupId = getPropertyValue(ROOT_SCHOOL_ADMINISTRATORS_GROUP);
		if (groupId != null) {
			rootGroup = getUserBusiness().getGroupHome().findByPrimaryKey(new Integer(groupId));
		} else {
			System.err.println("trying to store Commune Root school administrators group");
			rootGroup = getUserBusiness().getGroupBusiness().createGroup("Provider Administrators", "The Commune Root Provider Administrators Group.");
			setProperty(ROOT_SCHOOL_ADMINISTRATORS_GROUP, rootGroup.getPrimaryKey().toString());
		}
		return rootGroup;
	}

	/**
	 * Returns or creates (if not available) the default usergroup all other
	 * commune school administors have as their primary group.
	 *
	 * @throws CreateException
	 *             if it failed to create the group.
	 * @throws FinderException
	 *             if it failed to locate the group.
	 */

	@Override
	public Group getRootSchoolOtherCommuneAdministratorGroup() throws CreateException, FinderException, RemoteException {
		Group rootGroup = null;
		// create the default group
		String ROOT_SCHOOL_OTHER_COMMUNE_ADMINISTRATORS_GROUP = "school_administrators_other_commune_group_id";
		String groupId = getPropertyValue(ROOT_SCHOOL_OTHER_COMMUNE_ADMINISTRATORS_GROUP);
		if (groupId != null) {
			rootGroup = getUserBusiness().getGroupHome().findByPrimaryKey(new Integer(groupId));
		} else {
			System.err.println("trying to store Commune Root school other commune administrators group");
			rootGroup = getUserBusiness().getGroupBusiness().createGroup("School Administrators Other Commune", "The Commune Root School Other Commune Administrators Group.");
			setProperty(ROOT_SCHOOL_OTHER_COMMUNE_ADMINISTRATORS_GROUP, rootGroup.getPrimaryKey().toString());
		}
		return rootGroup;
	}

	/**
	 * Returns or creates (if not available) the default usergroup all other
	 * commune high school administors have as their primary group.
	 *
	 * @throws CreateException
	 *             if it failed to create the group.
	 * @throws FinderException
	 *             if it failed to locate the group.
	 */

	@Override
	public Group getRootHighSchoolOtherCommuneAdministratorGroup() throws CreateException, FinderException, RemoteException {
		Group rootGroup = null;

		// create the default group
		String ROOT_HIGH_SCHOOL_OTHER_COMMUNE_ADMINISTRATORS_GROUP = "high_school_administrators_other_commune_group_id";
		String groupId = getPropertyValue(ROOT_HIGH_SCHOOL_OTHER_COMMUNE_ADMINISTRATORS_GROUP);
		if (groupId != null) {
			rootGroup = getUserBusiness().getGroupHome().findByPrimaryKey(new Integer(groupId));
		} else {
			System.err.println("trying to store Commune Root high school other commune administrators group");
			rootGroup = getUserBusiness().getGroupBusiness().createGroup("High School Other Commune Administrators", "The Commune Root High School Other Commune Administrators Group.");
			setProperty(ROOT_HIGH_SCHOOL_OTHER_COMMUNE_ADMINISTRATORS_GROUP, rootGroup.getPrimaryKey().toString());
		}
		return rootGroup;
	}

	/**
	 * Returns or creates (if not available) the default usergroup all other
	 * commune provider(childcare) administors have as their primary group.
	 *
	 * @throws CreateException
	 *             if it failed to create the group.
	 * @throws FinderException
	 *             if it failed to locate the group.
	 */

	@Override
	public Group getRootProviderOtherCommuneAdministratorGroup() throws CreateException, FinderException, RemoteException {
		Group rootGroup = null;
		// create the default group
		String ROOT_SCHOOL_OTHER_COMMUNE_ADMINISTRATORS_GROUP = "provider_other_commune_administrators_group_id";
		String groupId = getPropertyValue(ROOT_SCHOOL_OTHER_COMMUNE_ADMINISTRATORS_GROUP);
		if (groupId != null) {
			rootGroup = getUserBusiness().getGroupHome().findByPrimaryKey(new Integer(groupId));
		} else {
			System.err.println("trying to store Commune Root provider other commune administrators group");
			rootGroup = getUserBusiness().getGroupBusiness().createGroup("Provider Other Commune Administrators", "The Commune Root Provider Other Commune Administrators Group.");
			setProperty(ROOT_SCHOOL_OTHER_COMMUNE_ADMINISTRATORS_GROUP, rootGroup.getPrimaryKey().toString());
		}
		return rootGroup;
	}

	/**
	 * Gets the groupID for a property name ... replaces the bundle properties
	 * that were used previously
	 *
	 * @param propertyName
	 * @return
	 */

	@Override
	public String getPropertyValue(String propertyName) {
		return this.getIWApplicationContext().getApplicationSettings().getProperty(propertyName);
	}

	/**
	 * sets a propertyName and value...
	 *
	 * @param propertyName
	 * @param propertyValue
	 */

	@Override
	public void setProperty(String propertyName, String propertyValue) {
		this.getIWApplicationContext().getApplicationSettings().setProperty(propertyName, propertyValue);
	}

	private Group getRootGroup(SchoolCategory category) {
		try {
			if (category.equals(getCategoryAdultEducation())) {
				return getRootAdultEducationAdministratorGroup();
			} else if (category.equals(getCategoryAfterSchoolCare()) || category.equals(getCategoryChildcare())) {
				return getRootProviderAdministratorGroup();
			} else if (category.equals(getCategoryElementarySchool())) {
				return getRootSchoolAdministratorGroup();
			} else if (category.equals(getCategoryMusicSchool())) {
				return getRootMusicSchoolAdministratorGroup();
			} else if (category.equals(getCategoryHighSchool())) {
				return getRootHighSchoolAdministratorGroup();
			}
		} catch (RemoteException re) {
			re.printStackTrace();
		} catch (CreateException e) {
			e.printStackTrace();
		} catch (FinderException e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * Returns or creates (if not available) the default usergroup all adult
	 * education administors have as their primary group.
	 *
	 * @throws CreateException
	 *             if it failed to create the group.
	 * @throws FinderException
	 *             if it failed to locate the group.
	 */

	@Override
	public Group getRootAdultEducationAdministratorGroup() throws CreateException, FinderException, RemoteException {
		Group rootGroup = null;

		// create the default group
		String ROOT_ADULT_EDUCATION_ADMINISTRATORS_GROUP = "adult_education_administrators_group_id";
		String groupId = getPropertyValue(ROOT_ADULT_EDUCATION_ADMINISTRATORS_GROUP);
		if (groupId != null) {
			rootGroup = getUserBusiness().getGroupHome().findByPrimaryKey(new Integer(groupId));
		} else {
			System.err.println("trying to store Commune Root Adult Education administrators group");
			rootGroup = getUserBusiness().getGroupBusiness().createGroup("Adult Education Administrators", "The Commune Root Adult Educaiton Administrators Group.");
			setProperty(ROOT_ADULT_EDUCATION_ADMINISTRATORS_GROUP, rootGroup.getPrimaryKey().toString());
		}
		return rootGroup;
	}

	@Override
	public void addSchoolAdministrator(User user) throws RemoteException {
		try {
			getUserBusiness().getGroupBusiness().addUser(((Integer) getRootSchoolAdministratorGroup().getPrimaryKey()).intValue(), user);
		} catch (FinderException fe) {
			throw new RemoteException("No root school administrator group found: " + fe.getMessage());
		} catch (CreateException ce) {
			throw new RemoteException("Could not set user with ID = " + user.getPrimaryKey().toString() + " as school administrator: " + ce.getMessage());
		}
	}

	@Override
	public SchoolYearPlaces getSchoolYearPlaces(Object primaryKey) {
		try {
			SchoolYearPlacesHome shome = (SchoolYearPlacesHome) IDOLookup.getHome(SchoolYearPlaces.class);
			return shome.findByPrimaryKey(primaryKey);
		} catch (Exception ex) {
			return null;
		}
	}

	@Override
	public void removeSchoolYearPlace(int id) {
		try {
			SchoolYearPlaces schoolYearPlaces = getSchoolYearPlaces(new Integer(id));
			schoolYearPlaces.remove();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	@Override
	public Collection findAllSchoolYearPlaces(int iSchoolId) {
		try {
			SchoolYearPlacesHome shome = getSchoolYearPlacesHome();
			return shome.findAllBySchool(iSchoolId);
		} catch (Exception ex) {
			ex.printStackTrace();
			return new java.util.Vector();
		}
	}

	@Override
	public Map getMapOfSchoolYearPlaces(int iSchoolId) {
		Collection c = findAllSchoolYearPlaces(iSchoolId);
		Map m = new HashMap();
		try {
			Iterator iter = c.iterator();
			while (iter.hasNext()) {
				SchoolYearPlaces p = (SchoolYearPlaces) iter.next();
				m.put(p.getPrimaryKey(), p);
			}
		} catch (Exception ex) {
		}
		return m;
	}

	@Override
	public SchoolYearPlacesHome getSchoolYearPlacesHome() {
		try {
			return (SchoolYearPlacesHome) IDOLookup.getHome(SchoolYearPlaces.class);
		} catch (IDOLookupException e) {
			throw new IBORuntimeException(e);
		}
	}

	@Override
	public void storeSchoolYearPlaces(int id, int school_id, int school_year_id, int places) throws java.rmi.RemoteException {
		SchoolYearPlacesHome shome = getSchoolYearPlacesHome();
		SchoolYearPlaces newSchoolYearPlaces;
		try {
			if (id > 0) {
				newSchoolYearPlaces = shome.findByPrimaryKey(new Integer(id));
			} else {
				newSchoolYearPlaces = shome.create();
			}
		} catch (javax.ejb.FinderException fe) {
			throw new java.rmi.RemoteException(fe.getMessage());
		} catch (javax.ejb.CreateException ce) {
			throw new java.rmi.RemoteException(ce.getMessage());
		}
		newSchoolYearPlaces.setSchoolId(school_id);
		newSchoolYearPlaces.setSchoolYearId(school_year_id);
		newSchoolYearPlaces.setPlaces(places);
		newSchoolYearPlaces.store();
	}

	@Override
	public SchoolYear getSchoolYear(Object primaryKey) {
		try {
			SchoolYearHome shome = getSchoolYearHome();
			return shome.findByPrimaryKey(primaryKey);
		} catch (Exception ex) {
			return null;
		}
	}

	@Override
	public void removeSchoolYear(int id) {
		removeSchoolYear(new Integer(id));
	}

	@Override
	public void removeSchoolYear(Object schoolYearPK) {
		try {
			SchoolYear area = getSchoolYear(schoolYearPK);
			area.remove();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	@Override
	public Collection findAllSchoolYears() {
		try {
			SchoolYearHome shome = getSchoolYearHome();
			return shome.findAllSchoolYears();
		} catch (Exception ex) {
			ex.printStackTrace();
			return new java.util.Vector();
		}
	}

	@Override
	public Collection findAllSchoolYearsBySchoolType(int schoolTypeId) {
		try {
			SchoolYearHome shome = getSchoolYearHome();
			return shome.findAllSchoolYearBySchoolType(schoolTypeId);
			// return shome.findAllSchoolYears();
		} catch (FinderException ex) {
			ex.printStackTrace();
			return new java.util.Vector();
		}
	}

	@Override
	public Collection findSchoolYearsBySchoolCategory(String schoolCategory) throws FinderException {
		return getSchoolYearHome().findBySchoolCategory(schoolCategory);
	}

	@Override
	public Collection findAllSchoolYearsByAge(int age) {
		try {
			SchoolYearHome shome = getSchoolYearHome();
			return shome.findAllByAge(age);
		} catch (Exception ex) {
			ex.printStackTrace();
			return new java.util.Vector();
		}
	}

	@Override
	public void storeSchoolYear(int pk, String name, int schoolTypeId, String category, String info, String localizedKey, int age) throws java.rmi.RemoteException {
		SchoolYearHome shome = getSchoolYearHome();
		SchoolYear newYear;
		try {
			if (pk > 0) {
				newYear = shome.findByPrimaryKey(new Integer(pk));
			} else {
				newYear = shome.create();
			}
		} catch (javax.ejb.FinderException fe) {
			throw new java.rmi.RemoteException(fe.getMessage());
		} catch (javax.ejb.CreateException ce) {
			throw new java.rmi.RemoteException(ce.getMessage());
		}
		newYear.setSchoolYearName(name);
		newYear.setSchoolYearInfo(info);
		newYear.setSchoolYearAge(age);
		if (schoolTypeId > 0) {
			newYear.setSchoolTypeId(schoolTypeId);
		}
		if (category != null) {
			newYear.setSchoolCategory(category);
		}
		if (localizedKey != null) {
			newYear.setLocalizedKey(localizedKey);
		}
		newYear.store();
	}

	@Override
	public SchoolYearHome getSchoolYearHome() {
		try {
			return (SchoolYearHome) IDOLookup.getHome(SchoolYear.class);
		} catch (IDOLookupException e) {
			throw new IBORuntimeException(e.getMessage());
		}
	}

	@Override
	public SchoolType getSchoolType(Object primaryKey) {
		try {
			SchoolTypeHome shome = (SchoolTypeHome) IDOLookup.getHome(SchoolType.class);
			return shome.findByPrimaryKey(primaryKey);
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

	@Override
	public Collection getSchoolTypesForCategory(SchoolCategory category, boolean showFreetimeTypes) {
		try {
			return getSchoolTypeHome().findAllByCategory(category.getCategory(), showFreetimeTypes);
		} catch (FinderException ex) {
			return null;
		}
	}

	@Override
	public void removeSchoolType(int id) {
		removeSchoolType(new Integer(id));
	}

	@Override
	public void removeSchoolType(Object schoolTypePK) {
		try {
			SchoolType type = getSchoolType(schoolTypePK);
			type.remove();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	@Override
	public Collection findAllSchoolTypes() {
		try {
			SchoolTypeHome shome = (SchoolTypeHome) IDOLookup.getHome(SchoolType.class);
			return shome.findAllSchoolTypes();
		} catch (Exception ex) {
			ex.printStackTrace();
			return com.idega.util.ListUtil.getEmptyList();
		}
	}

	@Override
	public Collection findAllSchoolTypesInCategory(String Category) {
		return findAllSchoolTypesInCategory(Category, true);
	}

	@Override
	public Collection findAllSchoolTypesInCategory(String Category, boolean showFreetimeTypes) {
		try {
			SchoolTypeHome shome = (SchoolTypeHome) IDOLookup.getHome(SchoolType.class);
			return shome.findAllByCategory(Category, showFreetimeTypes);
		} catch (Exception ex) {
			ex.printStackTrace();
			return com.idega.util.ListUtil.getEmptyList();
		}
	}

	@Override
	public Collection findAllSchoolTypesInCategoryFreeTime(String Category) {
		try {
			SchoolTypeHome shome = (SchoolTypeHome) IDOLookup.getHome(SchoolType.class);
			return shome.findAllFreetimeTypes(Category);
		} catch (Exception ex) {
			ex.printStackTrace();
			return com.idega.util.ListUtil.getEmptyList();
		}
	}

	/**
	 * @return The School type key registered for after school care school
	 *         types.
	 */

	@Override
	public String getAfterSchoolCareSchoolCategory() {
		SchoolCategory category = getCategoryAfterSchoolCare();
		if (category != null) {
			return category.getCategory();
		}
		return null;
	}

	/**
	 * @return The School type key registered for Childcare school types.
	 */

	@Override
	public String getChildCareSchoolCategory() {
		SchoolCategory category = getCategoryChildcare();
		if (category != null) {
			return category.getCategory();
		}
		return null;
	}

	/**
	 * @return The School type key registered for Elementary school types.
	 */

	@Override
	public String getElementarySchoolSchoolCategory() {
		SchoolCategory category = getCategoryElementarySchool();
		if (category != null) {
			return category.getCategory();
		}
		return null;
	}

	@Override
	public String getAdultEducationSchoolCategory() {
		SchoolCategory category = getCategoryAdultEducation();
		if (category != null) {
			return category.getCategory();
		}
		return null;
	}

	/**
	 * @return The School type key registered for Highschool types.
	 */

	@Override
	public String getHighSchoolSchoolCategory() {
		SchoolCategory category = getCategoryHighSchool();
		if (category != null) {
			return category.getCategory();
		}
		return null;
	}

	@Override
	public Collection findAllSchoolTypesForChildCare() {
		return findAllSchoolTypesInCategory(getChildCareSchoolCategory());
	}

	@Override
	public Collection findAllSchoolTypesForSchool() {
		return findAllSchoolTypesInCategory(getElementarySchoolSchoolCategory());
	}

	@Override
	public Collection findAllSchoolTypesForAdultEducation() {
		return findAllSchoolTypesInCategory(getAdultEducationSchoolCategory());
	}

	@Override
	public void storeSchoolType(int id, String name, String info, String category, String locKey, int maxAge, boolean isFreetimeType, boolean isFamilyFreetimeType, int order) throws java.rmi.RemoteException {
		storeSchoolType(id, name, info, category, locKey, maxAge, isFreetimeType, isFamilyFreetimeType, order, null);
	}

	@Override
	public void storeSchoolType(int id, String name, String info, String category, String locKey, int maxAge, boolean isFreetimeType, boolean isFamilyFreetimeType, int order, String typeStringId) throws java.rmi.RemoteException {
		SchoolTypeHome shome = (SchoolTypeHome) IDOLookup.getHome(SchoolType.class);
		SchoolType newType;
		try {
			if (id > 0) {
				newType = shome.findByPrimaryKey(new Integer(id));
			} else {
				newType = shome.create();
			}
		} catch (javax.ejb.FinderException fe) {
			throw new java.rmi.RemoteException(fe.getMessage());
		} catch (javax.ejb.CreateException ce) {
			throw new java.rmi.RemoteException(ce.getMessage());
		}
		newType.setSchoolTypeInfo(info);
		newType.setSchoolTypeName(name);
		newType.setSchoolCategory(category);
		newType.setLocalizationKey(locKey);
		if (maxAge != -1) {
			newType.setMaxSchoolAge(maxAge);
		}
		newType.setIsFreetimeType(isFreetimeType);
		newType.setIsFamilyFreetimeType(isFamilyFreetimeType);
		newType.setOrder(order);
		if (typeStringId != null) {
			newType.setTypeStringId(typeStringId);
		}
		newType.store();
	}

	@Override
	public SchoolSeason getSchoolSeason(Object primaryKey) {
		try {
			SchoolSeasonHome shome = (SchoolSeasonHome) IDOLookup.getHome(SchoolSeason.class);
			return shome.findByPrimaryKey(primaryKey);
		} catch (Exception ex) {
			return null;
		}
	}

	@Override
	public SchoolSeason getCurrentSchoolSeason(SchoolCategory category) throws FinderException {
		return getSchoolSeasonHome().findCurrentSeason(category);
	}

	@Override
	public SchoolSeason getNextSchoolSeason(SchoolCategory category) throws FinderException {
		return getSchoolSeasonHome().findNextSeason(category, new IWTimestamp().getDate());
	}

	@Override
	public void removeSchoolSeason(int id) {
		removeSchoolSeason(new Integer(id));
	}

	@Override
	public void removeSchoolSeason(Object schoolSeasonPK) {
		try {
			SchoolSeason season = getSchoolSeason(schoolSeasonPK);
			season.remove();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	@Override
	public Collection findAllSchoolSeasons() {
		return findAllSchoolSeasons((SchoolCategory) null);
	}

	@Override
	public Collection findAllSchoolSeasons(String schoolCategory) {
		try {
			return findAllSchoolSeasons(getSchoolCategoryHome().findByPrimaryKey(schoolCategory));
		} catch (FinderException fe) {
			fe.printStackTrace();
			return new ArrayList();
		}
	}

	@Override
	public Collection findAllSchoolSeasons(SchoolCategory category) {
		try {
			SchoolSeasonHome shome = (SchoolSeasonHome) IDOLookup.getHome(SchoolSeason.class);
			return shome.findAllSchoolSeasons(category);
		} catch (Exception ex) {
			ex.printStackTrace();
			return new java.util.Vector();
		}
	}

	/**
	 * Returns all season where the due_date >= TODAY and start_date <= TODAY.
	 *
	 * @param category
	 * @return
	 */

	@Override
	public Collection findAllCurrentSeasons(SchoolCategory category) {
		try {
			SchoolSeasonHome shome = (SchoolSeasonHome) IDOLookup.getHome(SchoolSeason.class);
			return shome.findCurrentSchoolSeasons(category);
		} catch (Exception ex) {
			ex.printStackTrace();
			return new java.util.Vector();
		}
	}

	@Override
	public Collection findAllPreviousSchoolSeasons(SchoolSeason schoolSeason) {
		try {
			SchoolSeasonHome shome = (SchoolSeasonHome) IDOLookup.getHome(SchoolSeason.class);
			return shome.findAllPreviousSchoolSeasons(schoolSeason);
		} catch (Exception ex) {
			ex.printStackTrace();
			return new java.util.Vector();
		}
	}

	@Override
	public Collection findAllPreviousSchoolSeasons(int schoolSeasonID) {
		try {
			SchoolSeason season = getSchoolSeasonHome().findByPrimaryKey(new Integer(schoolSeasonID));
			return getSchoolSeasonHome().findAllPreviousSchoolSeasons(season);
		} catch (FinderException fe) {
			return new Vector();
		}
	}

	@Override
	public SchoolSeason findPreviousSchoolSeason(int schoolSeasonID) {
		try {
			SchoolSeason season = getSchoolSeasonHome().findByPrimaryKey(new Integer(schoolSeasonID));
			return getSchoolSeasonHome().findPreviousSchoolSeason(season);
		} catch (FinderException fe) {
			return null;
		}
	}

	@Override
	public SchoolSeason storeSchoolSeason(int id, String name, Date start, Date end, Date choiceStartDate, Date choiceEndDate, String category, int externalID) throws java.rmi.RemoteException {
		SchoolSeasonHome shome = (SchoolSeasonHome) IDOLookup.getHome(SchoolSeason.class);
		SchoolSeason newSeason;
		try {
			if (id > 0) {
				newSeason = shome.findByPrimaryKey(new Integer(id));
			} else {
				newSeason = shome.create();
			}
		} catch (javax.ejb.FinderException fe) {
			throw new java.rmi.RemoteException(fe.getMessage());
		} catch (javax.ejb.CreateException ce) {
			throw new java.rmi.RemoteException(ce.getMessage());
		}
		newSeason.setSchoolSeasonName(name);
		newSeason.setSchoolSeasonStart(start);
		newSeason.setSchoolSeasonEnd(end);
		newSeason.setChoiceStartDate(choiceStartDate);
		newSeason.setChoiceEndDate(choiceEndDate);
		newSeason.setSchoolCategory(category);
		newSeason.setExternalID(externalID);
		newSeason.store();
		return newSeason;
	}

	@Override
	public SchoolClassMember findClassMemberInClass(int studentID, int schoolClassID) {
		try {
			return getSchoolClassMemberHome().findByUserAndSchoolClass(studentID, schoolClassID);
		} catch (FinderException fe) {
			return null;
		}
	}

	@Override
	public SchoolClassMember findByStudentAndSeason(int userID, int seasonID) {
		try {
			return getSchoolClassMemberHome().findByUserAndSeason(userID, seasonID);
		} catch (FinderException fe) {
			return null;
		}
	}

	@Override
	public SchoolClassMember findByStudentAndSchoolAndSeason(int userID, int schoolID, int seasonID) {
		try {
			return getSchoolClassMemberHome().findByUserAndSchoolAndSeason(userID, schoolID, seasonID);
		} catch (FinderException fe) {
			return null;
		}
	}

	@Override
	public SchoolClassMember findByStudentAndSeason(User user, SchoolSeason season) {
		try {
			return getSchoolClassMemberHome().findByUserAndSeason(user, season);
		} catch (FinderException fe) {
			return null;
		}
	}

	@Override
	public SchoolClassMember findByStudentAndSeason(SchoolClassMember student, SchoolSeason season) {
		try {
			return getSchoolClassMemberHome().findByUserAndSeason(student.getClassMemberId(), ((Integer) season.getPrimaryKey()).intValue());
		} catch (FinderException fe) {
			return null;
		}
	}

	@Override
	public Collection findClassMember(int studentID) {
		try {
			return getSchoolClassMemberHome().findByStudent(studentID);
		} catch (FinderException fe) {
			return new Vector();
		}
	}

	@Override
	public Collection findSubGroupPlacements(int studentID, int schoolID, int seasonID) throws FinderException {
		return getSchoolClassMemberHome().findAllSubGroupPlacements(studentID, schoolID, seasonID);
	}

	@Override
	public Collection findClassMemberInSchool(int studentID, int schoolID) {
		try {
			Collection types = findAllSchoolTypesForSchool();
			return getSchoolClassMemberHome().findByStudentAndSchoolAndTypes(studentID, schoolID, types);
		} catch (FinderException fe) {
			return new Vector();
		}
	}

	@Override
	public Collection findClassMemberInAdultEducation(int studentID, int schoolID) {
		try {
			Collection types = findAllSchoolTypesForAdultEducation();
			return getSchoolClassMemberHome().findByStudentAndSchoolAndTypes(studentID, schoolID, types);
		} catch (FinderException fe) {
			return new Vector();
		}
	}

	@Override
	public Collection findClassMemberInChildCare(int studentID, int schoolID) {
		try {
			Collection types = findAllSchoolTypesForChildCare();
			return getSchoolClassMemberHome().findByStudentAndSchoolAndTypes(studentID, schoolID, types);
		} catch (FinderException fe) {
			return new Vector();
		}
	}

	@Override
	public Collection findStudentsInClass(int studentClassID) {
		try {
			return getSchoolClassMemberHome().findBySchoolClass(studentClassID);
		} catch (FinderException fe) {
			return new Vector();
		}
	}

	@Override
	public Collection findStudentsInClassAndYear(int studentClassID, int schoolYearID) {
		try {
			return getSchoolClassMemberHome().findBySchoolClassAndYear(studentClassID, schoolYearID);
		} catch (FinderException fe) {
			return new Vector();
		}
	}

	@Override
	public Collection findStudentsInSchool(int schoolID, int schoolClassID) {
		try {
			return getSchoolClassMemberHome().findBySchool(schoolID, schoolClassID);
		} catch (FinderException e) {
			return new Vector();
		}
	}

	@Override
	public Collection findSchoolByDate(int schoolID, int schoolClassID, java.sql.Date date) {
		try {
			return getSchoolClassMemberHome().findBySchool(schoolID, schoolClassID, date);
		} catch (FinderException e) {
			return new Vector();
		}
	}

	@Override
	public Collection findStudentsInSchoolByDate(int schoolID, int schoolClassID, java.sql.Date date, boolean showNotYetActive) {
		return findStudentsInSchoolByDate(schoolID, schoolClassID, null, date, showNotYetActive);
	}

	@Override
	public Collection findStudentsInSchoolByDate(int schoolID, int schoolClassID, String schoolCategory, java.sql.Date date, boolean showNotYetActive) {
		try {
			return getSchoolClassMemberHome().findBySchoolAndLog(schoolID, schoolClassID, schoolCategory, date, showNotYetActive);
		} catch (FinderException e) {
			return new Vector();
		}
	}

	@Override
	public Collection findStudentsInSchoolByDateChildcare(int schoolID, int schoolClassID, String schoolCategory, java.sql.Date date, boolean showNotYetActive) {
		try {
			return getSchoolClassMemberHome().findBySchoolChildcare(schoolID, schoolClassID, schoolCategory, date, showNotYetActive);
		} catch (FinderException e) {
			return new Vector();
		}
	}

	@Override
	public Collection findStudentsInSchoolByDate(int schoolID, int schoolClassID, java.sql.Date date) {
		return findStudentsInSchoolByDate(schoolID, schoolClassID, null, date);
	}

	@Override
	public Collection findStudentsInSchoolByDate(int schoolID, int schoolClassID, String schoolCategory, java.sql.Date date) {
		try {
			return getSchoolClassMemberHome().findBySchoolAndLog(schoolID, schoolClassID, schoolCategory, date);
		} catch (FinderException e) {
			return new Vector();
		}
	}

	@Override
	public SchoolClassMember findSchoolClassMember(int userID, int schoolClassID) {
		try {
			return getSchoolClassMemberHome().findByUserAndSchoolClass(userID, schoolClassID);
		} catch (FinderException e) {
			return null;
		}
	}

	@Override
	public Collection findStudentsBySchoolAndSeasonAndYear(int schoolID, int seasonID, int yearID) {
		try {
			return getSchoolClassMemberHome().findBySchoolAndSeasonAndYear(schoolID, seasonID, yearID);
		} catch (FinderException fe) {
			return new Vector();
		}
	}

	@Override
	public Collection findStudentsBySchoolAndSeasonAndYear(School school, SchoolSeason season, SchoolYear year) {
		try {
			return getSchoolClassMemberHome().findBySchoolAndSeasonAndYearAndStudyPath(school, season, year, null);
		} catch (FinderException fe) {
			return new Vector();
		}
	}

	@Override
	public Collection findStudentsBySchoolAndSeason(int schoolID, int seasonID) {
		try {
			return getSchoolClassMemberHome().findBySchoolAndSeason(schoolID, seasonID);
		} catch (FinderException fe) {
			return new Vector();
		}
	}

	@Override
	public void removeSchoolClassMemberFromClass(int studentID, int schoolClassID) {
		try {
			SchoolClass group = this.findSchoolClass(new Integer(schoolClassID));
			if (group.getIsSubGroup()) {
				try {
					SchoolClassMember member = getSchoolClassMemberHome().findByUserAndSchoolAndSeason(studentID, group.getSchoolId(), group.getSchoolSeasonId(), getSchoolTypesForCategory(getCategoryElementarySchool(), false));
					member.removeFromGroup(group);
				} catch (FinderException fe) {
					fe.printStackTrace();
				} catch (IDORemoveRelationshipException irre) {
					irre.printStackTrace();
				}
			} else {
				SchoolClassMember member = getSchoolClassMemberHome().findByUserAndSchoolClass(studentID, schoolClassID);
				try {
					Collection groups = member.getSubGroups();
					Iterator iter = groups.iterator();
					while (iter.hasNext()) {
						SchoolClass subGroup = (SchoolClass) iter.next();
						try {
							member.removeFromGroup(subGroup);
						} catch (IDORemoveRelationshipException irre2) {
							irre2.printStackTrace();
						}
					}
				} catch (IDORelationshipException ire) {
					ire.printStackTrace();
				}
				try {
					member.remove();
				} catch (RemoveException re) {
					re.printStackTrace();
				}
			}
		} catch (FinderException fe) {
		}
	}

	@Override
	public void removeSchoolClassMember(int studentID) {
		Collection members = findClassMember(studentID);
		if (!members.isEmpty()) {
			Iterator iter = members.iterator();
			while (iter.hasNext()) {
				SchoolClassMember member = (SchoolClassMember) iter.next();
				removeSchoolClassMemberFromClass(((Integer) member.getPrimaryKey()).intValue(), member.getSchoolClassId());
			}
		}
	}

	/**
	 * This method is used primarily to get the SchoolTypeId when using
	 * storeSchoolClassMember() below:
	 *
	 * @param schoolClassID
	 * @return returns -1 if typeId is not found
	 * @throws RemoteException
	 */

	@Override
	public int getSchoolTypeIdFromSchoolClass(int schoolClassID) {
		int schoolTypeId = -1;
		if (schoolClassID != -1) {
			SchoolClass group = null;
			try {
				group = getSchoolClassHome().findByPrimaryKey(new Integer(schoolClassID));
			} catch (FinderException e) {
				e.printStackTrace();
			}
			if (group != null) {
				schoolTypeId = group.getSchoolTypeId();
			}
		}
		return schoolTypeId;
	}

	/**
	 * Used by ChildCare placements that don't store school year
	 */

	@Override
	public SchoolClassMember storeSchoolClassMemberCC(int studentID, int schoolClassID, int schoolTypeID, Timestamp registerDate, int registrator) {
		return storeSchoolClassMember(studentID, schoolClassID, -1, schoolTypeID, registerDate, registrator, null);
	}

	/**
	 * Used by ChildCare placements that don't store school year
	 */

	@Override
	public SchoolClassMember storeSchoolClassMemberCC(int studentID, int schoolClassID, int schoolTypeID, Timestamp registerDate, int registrator, String notes) {
		return storeSchoolClassMember(studentID, schoolClassID, -1, schoolTypeID, registerDate, null, registrator, notes);
	}

	/**
	 * Used by ChildCare placements that don't store school year
	 */

	@Override
	public SchoolClassMember storeSchoolClassMemberCC(int studentID, int schoolClassID, int schoolTypeID, Timestamp registerDate, Timestamp removedDate, int registrator, String notes) {
		return storeSchoolClassMember(studentID, schoolClassID, -1, schoolTypeID, registerDate, removedDate, registrator, notes);
	}

	@Override
	public SchoolClassMember storeSchoolClassMember(int studentID, int schoolClassID, int schoolYearID, int schoolTypeID, Timestamp registerDate, int registrator) {
		return storeSchoolClassMember(studentID, schoolClassID, schoolYearID, schoolTypeID, registerDate, registrator, null);
	}

	// ny

	@Override
	public SchoolClassMember storeSchoolClassMember(int studentID, int schoolClassID, int schoolYearID, int schoolTypeID, Timestamp registerDate, int registrator, int studyPathID) {
		return storeSchoolClassMember(studentID, schoolClassID, schoolYearID, schoolTypeID, registerDate, null, registrator, null, null, studyPathID, -1);
	}

	@Override
	public SchoolClassMember storeSchoolClassMember(int studentID, int schoolClassID, int schoolYearID, int schoolTypeID, Timestamp registerDate, int registrator, String notes) {
		return storeSchoolClassMember(studentID, schoolClassID, schoolYearID, schoolTypeID, registerDate, null, registrator, notes);
	}

	@Override
	public SchoolClassMember storeSchoolClassMember(int studentID, int schoolClassID, int schoolYearID, int schoolTypeID, Timestamp registerDate, Timestamp removedDate, int registrator, String notes) {
		return storeSchoolClassMember(studentID, schoolClassID, schoolYearID, schoolTypeID, registerDate, removedDate, registrator, notes, null);
	}

	@Override
	public SchoolClassMember storeSchoolClassMember(int studentID, int schoolClassID, int schoolYearID, int schoolTypeID, Timestamp registerDate, Timestamp removedDate, int registrator, String notes, String language) {
		return storeSchoolClassMember(studentID, schoolClassID, schoolYearID, schoolTypeID, registerDate, removedDate, registrator, notes, language, -1, -1);
	}

	/**
	 * Stores placement. If placement for student and schoolgroup exist
	 * placement is updated
	 */

	@Override
	public SchoolClassMember storeSchoolClassMember(int studentID, int schoolClassID, int schoolYearID, int schoolTypeID, Timestamp registerDate, Timestamp removedDate, int registrator, String notes, String language, int studyPathID, int handicraftId) {
		try {
			SchoolClass group = this.findSchoolClass(new Integer(schoolClassID));
			if (group.getIsSubGroup()) {
				try {
					SchoolClassMember member = getSchoolClassMemberHome().findByUserAndSchoolAndSeason(studentID, group.getSchoolId(), group.getSchoolSeasonId(), getSchoolTypesForCategory(getCategoryElementarySchool(), false));
					member.addToGroup(group);
					return member;
				} catch (FinderException fe) {
					fe.printStackTrace();
				} catch (IDOAddRelationshipException iare) {
					iare.printStackTrace();
				}
				return null;
			} else {
				boolean updateStartDate = true;
				SchoolClassMember member = findClassMemberInClass(studentID, schoolClassID);
				if (member != null) {
					if (member.getRemovedDate() != null) {
						IWTimestamp start = new IWTimestamp(registerDate);
						IWTimestamp end = new IWTimestamp(member.getRemovedDate());
						if (IWTimestamp.getDaysBetween(end, start) <= 1) {
							updateStartDate = false;
						} else {
							member = null;
						}
					}
				}
				if (member == null) {
					member = this.getSchoolClassMemberHome().create();
				}
				if (member != null) {
					member.setClassMemberId(studentID);
					member.setSchoolClassId(schoolClassID);
					if (schoolYearID > 0) {
						member.setSchoolYear(schoolYearID);
					}
					if (schoolTypeID > 0) {
						member.setSchoolTypeId(schoolTypeID);
					}
					if (updateStartDate) {
						member.setRegisterDate(registerDate);
					}
					member.setRemovedDate(removedDate);
					if (registrator != -1) {
						member.setRegistratorId(registrator);
					}
					if (notes != null) {
						member.setNotes(notes);
					}
					if (language != null) {
						member.setLanguage(language);
					}
					if (studyPathID != -1) {
						member.setStudyPathId(studyPathID);
					}
					if (handicraftId > 0) {
						member.setHandicraftId(handicraftId);
					}
					member.setRegistrationCreatedDate(IWTimestamp.getTimestampRightNow());
					member.store();
				}
				return member;
			}
		} catch (CreateException ce) {
			ce.printStackTrace(System.err);
			return null;
		}
	}

	@Override
	public SchoolClassMember storeNewSchoolClassMember(int studentID, int schoolClassID, int schoolYearID, int schoolTypeID, Timestamp registerDate, int registrator, String notes, String languageID) {
		return storeNewSchoolClassMember(studentID, schoolClassID, schoolYearID, schoolTypeID, registerDate, null, registrator, notes, languageID);
	}

	/**
	 * Creates and stores a new placment
	 *
	 * @param studentID
	 * @param schoolClassID
	 * @param schoolYearID
	 * @param schoolTypeID
	 * @param registerDate
	 * @param removedDate
	 * @param registrator
	 * @param notes
	 * @return
	 * @throws RemoteException
	 */

	@Override
	public SchoolClassMember storeNewSchoolClassMember(int studentID, int schoolClassID, int schoolYearID, int schoolTypeID, Timestamp registerDate, Timestamp removedDate, int registrator, String notes, String sLanguage) {
		try {
			SchoolClassMember member = getSchoolClassMemberHome().create();
			if (member != null) {
				member.setClassMemberId(studentID);
				member.setSchoolClassId(schoolClassID);
				if (schoolYearID > 0) {
					member.setSchoolYear(schoolYearID);
				}
				if (schoolTypeID > 0) {
					member.setSchoolTypeId(schoolTypeID);
				}
				if (registerDate != null) {
					member.setRegisterDate(registerDate);
				}
				if (removedDate != null) {
					member.setRemovedDate(removedDate);
				}
				if (registrator != -1) {
					member.setRegistratorId(registrator);
				}
				if (notes != null) {
					member.setNotes(notes);
				}
				if (sLanguage != null) {
					member.setLanguage(sLanguage);
				}
				member.setRegistrationCreatedDate(IWTimestamp.getTimestampRightNow());
				member.store();
			}
			return member;
		} catch (CreateException ce) {
			ce.printStackTrace(System.err);
			return null;
		}
	}

	@Override
	public SchoolClass findSchoolClass(Object primaryKey) {
		try {
			return getSchoolClassHome().findByPrimaryKey(primaryKey);
		} catch (FinderException fe) {
			return null;
		}
	}

	@Override
	public Collection findSchoolClassesBySchool(int schoolID) {
		try {
			return getSchoolClassHome().findBySchool(schoolID);
		} catch (FinderException fe) {
			return new Vector();
		}
	}

	@Override
	public Collection findSchoolClassesBySchoolAndCategory(int schoolID, String category) {
		try {
			return getSchoolClassHome().findBySchoolAndCategory(schoolID, category);
		} catch (FinderException fe) {
			return new Vector();
		}
	}

	@Override
	public Collection findElementarySchoolClassesBySchool(int schoolID) {
		return findSchoolClassesBySchoolAndCategory(schoolID, getElementarySchoolSchoolCategory());
	}

	@Override
	public Collection findChildcareClassesBySchool(int schoolID) {
		return findSchoolClassesBySchoolAndCategory(schoolID, getChildCareSchoolCategory());
	}

	@Override
	public Collection findAfterschoolClassesBySchool(int schoolID) {
		return findSchoolClassesBySchoolAndCategory(schoolID, getAfterSchoolCareSchoolCategory());
	}

	@Override
	public Collection findSchoolClassesBySchoolAndSeason(int schoolID, int schoolSeasonID) {
		try {
			return getSchoolClassHome().findBySchoolAndSeason(schoolID, schoolSeasonID);
		} catch (FinderException fe) {
			return new Vector();
		}
	}

	@Override
	public Collection findSchoolClassesBySchoolAndYear(int schoolID, int schoolYearID) {
		try {
			return getSchoolClassHome().findBySchoolAndYear(schoolID, schoolYearID);
		} catch (FinderException fe) {
			fe.printStackTrace();
			return new ArrayList();
		}
	}

	@Override
	public Collection findSchoolClassesBySchoolAndSeasonAndYear(int schoolID, int schoolSeasonID, int schoolYearID, boolean showSubGroups) {
		try {
			return getSchoolClassHome().findBySchoolAndSeasonAndYear(schoolID, schoolSeasonID, schoolYearID, showSubGroups);
		} catch (FinderException fe) {
			fe.printStackTrace();
			return new ArrayList();
		}
	}

	@Override
	public Collection findSchoolClassesBySchoolAndSchoolTypeAndSeason(int schoolID, int schoolTypeID, int schoolSeasonID, Boolean showSubGroups, Boolean showNonSeasonGroups) {
		try {
			return getSchoolClassHome().findBySchoolAndSchoolTypeAndSeason(schoolID, schoolTypeID, schoolSeasonID, showSubGroups, showNonSeasonGroups);
		} catch (FinderException fe) {
			fe.printStackTrace();
			return new ArrayList();
		}
	}

	@Override
	public Collection findSchoolClassesBySchoolAndSeasonAndYear(int schoolID, int schoolSeasonID, int schoolYearID) {
		try {
			return getSchoolClassHome().findBySchoolAndSeasonAndYear(schoolID, schoolSeasonID, schoolYearID);
		} catch (FinderException fe) {
			fe.printStackTrace();
			return new ArrayList();
		}
	}

	@Override
	public Collection findSchoolClassesBySchoolAndSeasonAndYearAndStudyPath(int schoolID, int schoolSeasonID, int schoolYearID, int studyPathID) {
		try {
			return getSchoolClassHome().findBySchoolAndSeasonAndInYear(schoolID, schoolSeasonID, schoolYearID, studyPathID);
		} catch (FinderException fe) {
			fe.printStackTrace();
			return new ArrayList();
		}
	}

	@Override
	public Collection findSchoolClassesBySchoolAndSeasonAndYears(int schoolID, int schoolSeasonID, String[] schoolYearIDs) {
		try {
			return getSchoolClassHome().findBySchoolAndSeasonAndYears(schoolID, schoolSeasonID, schoolYearIDs);
		} catch (FinderException fe) {
			return new ArrayList();
		}
	}

	@Override
	public Collection findSchoolClassesBySchoolAndSeasonAndYears(int schoolID, int schoolSeasonID, String[] schoolYearIDs, boolean showSubGroups) {
		try {
			return getSchoolClassHome().findBySchoolAndSeasonAndYears(schoolID, schoolSeasonID, schoolYearIDs, showSubGroups);
		} catch (FinderException fe) {
			return new ArrayList();
		}
	}

	@Override
	public Collection findSchoolClassesByTeacher(int teacherID) {
		try {
			return getSchoolClassHome().findByTeacher(teacherID);
		} catch (FinderException fe) {
			return new Vector();
		}
	}

	@Override
	public Collection findSubGroupPlacements(SchoolClass group) {
		try {
			return group.getSubGroupPlacements();
		} catch (IDORelationshipException ire) {
			return new ArrayList();
		}
	}

	@Override
	public Collection findSchoolClassesBySchoolAndTeacher(int schoolID, int teacherID) {
		try {
			return getSchoolClassHome().findBySchoolAndTeacher(schoolID, teacherID);
		} catch (FinderException fe) {
			return new Vector();
		}
	}

	@Override
	public Collection findSchoolClassesBySchoolAndSeasonAndTeacher(int schoolID, int schoolSeasonID, int teacherID) {
		try {
			return getSchoolClassHome().findBySchoolAndSeasonAndTeacher(schoolID, schoolSeasonID, teacherID);
		} catch (FinderException fe) {
			return new Vector();
		}
	}

	@Override
	public int getNumberOfStudentsInClass(int schoolClassID) {
		try {
			return getSchoolClassHome().getNumberOfStudentsInClass(schoolClassID);
		} catch (IDOException ie) {
			return 0;
		}
	}

	@Override
	public void removeSchoolClass(int schoolClassID) {
		try {
			SchoolClass schoolClass = getSchoolClassHome().findByPrimaryKey(new Integer(schoolClassID));
			schoolClass.remove();
		} catch (FinderException fe) {
			fe.printStackTrace(System.err);
		} catch (RemoveException re) {
			re.printStackTrace(System.err);
		}
	}

	@Override
	public void invalidateSchoolClass(int schoolClassID) {
		try {
			SchoolClass schoolClass = getSchoolClassHome().findByPrimaryKey(new Integer(schoolClassID));
			schoolClass.setValid(false);
			schoolClass.store();
			UserTransaction trans = getSessionContext().getUserTransaction();
			try {
				trans.begin();
				Collection students = findStudentsInClass(schoolClassID);
				Iterator iter = students.iterator();
				while (iter.hasNext()) {
					SchoolClassMember element = (SchoolClassMember) iter.next();
					element.remove();
				}
			} catch (Exception ex) {
				try {
					trans.rollback();
				} catch (javax.transaction.SystemException e) {
				}
			}
		} catch (FinderException fe) {
			fe.printStackTrace(System.err);
		}
	}

	@Override
	public SchoolClass storeSchoolClass(int schoolClassID, String className, int schoolID, int schoolTypeID, int seasonID, String[] schoolYearIDs, String[] teacherIDs) {
		return storeSchoolClass(schoolClassID, className, schoolID, schoolTypeID, seasonID, schoolYearIDs, teacherIDs, null);
	}

	@Override
	public SchoolClass storeSchoolClass(int schoolClassID, String className, int schoolID, int schoolTypeID, int seasonID, String[] schoolYearIDs, String[] teacherIDs, String[] studyPathIDs) {
		return storeSchoolClass(schoolClassID, className, schoolID, schoolTypeID, seasonID, schoolYearIDs, teacherIDs, studyPathIDs, null);
	}

	@Override
	public SchoolClass storeSchoolClass(int schoolClassID, String className, int schoolID, int schoolTypeID, int seasonID, String[] schoolYearIDs, String[] teacherIDs, String[] studyPathIDs, String groupStringId) {
		SchoolClass schoolClass = null;
		try {
			schoolClass = getSchoolClassHome().findByPrimaryKey(new Integer(schoolClassID));
		} catch (FinderException e) {
			try {
				schoolClass = getSchoolClassHome().create();
			} catch (CreateException e1) {
				e1.printStackTrace();
				return null;
			}
		}
		schoolClass.setSchoolClassName(className);
		schoolClass.setSchoolId(schoolID);
		if (schoolTypeID != -1) {
			schoolClass.setSchoolTypeId(schoolTypeID);
		}
		if (seasonID != -1) {
			schoolClass.setSchoolSeasonId(seasonID);
		}
		schoolClass.setValid(true);
		if (groupStringId != null) {
			schoolClass.setGroupStringId(groupStringId);
		}
		schoolClass.store();
		if (schoolYearIDs != null) {
			Collection years = null;
			try {
				years = getSchoolYearHome().findAllByIDs(schoolYearIDs);
			} catch (FinderException e1) {
				years = new ArrayList();
			}
			try {
				schoolClass.removeFromSchoolYear();
			} catch (IDORemoveRelationshipException e1) {
				e1.printStackTrace();
			}
			Iterator iter = years.iterator();
			while (iter.hasNext()) {
				SchoolYear year = (SchoolYear) iter.next();
				try {
					schoolClass.addSchoolYear(year);
				} catch (IDOAddRelationshipException iare) {
					iare.printStackTrace();
				}
			}
		}
		if (teacherIDs != null) {
			Collection newTeachers = null;
			try {
				newTeachers = getUserBusiness().getUsers(teacherIDs);
				if (newTeachers == null) {
					newTeachers = new ArrayList();
				}
			} catch (RemoteException e1) {
				newTeachers = new ArrayList();
			}
			try {
				schoolClass.removeFromUser();
			} catch (IDORemoveRelationshipException e2) {
				e2.printStackTrace();
			}
			Iterator iter = newTeachers.iterator();
			while (iter.hasNext()) {
				User teacher = (User) iter.next();
				try {
					schoolClass.addTeacher(teacher);
				} catch (IDOAddRelationshipException iare) {
					iare.printStackTrace();
				}
			}
		}
		if (studyPathIDs != null) {
			Collection studyPaths = null;
			try {
				studyPaths = getSchoolStudyPathHome().findAllByIDs(studyPathIDs);
			} catch (FinderException e1) {
				studyPaths = new ArrayList();
			}
			try {
				schoolClass.removeStudyPaths();
			} catch (IDORemoveRelationshipException e1) {
				e1.printStackTrace();
			}
			Iterator iter = studyPaths.iterator();
			while (iter.hasNext()) {
				SchoolStudyPath studyPath = (SchoolStudyPath) iter.next();
				try {
					schoolClass.addStudyPath(studyPath);
				} catch (IDOAddRelationshipException iare) {
					iare.printStackTrace();
				}
			}
		}
		return schoolClass;
	}

	@Override
	public SchoolClass storeSchoolClass(int schoolClassID, String className, int schoolID, int schoolSeasonID, int schoolYearID, int teacherID) {
		try {
			SchoolClass schoolClass;
			if (schoolClassID != -1) {
				schoolClass = getSchoolClassHome().findByPrimaryKey(new Integer(schoolClassID));
			} else {
				schoolClass = getSchoolClassHome().create();
				schoolClass.store(); // so it gets a primary key, otherwise an
				// exception
				// is thrown when school year is added
			}
			schoolClass.setSchoolClassName(className);
			schoolClass.setSchoolId(schoolID);
			if (schoolSeasonID != -1) {
				schoolClass.setSchoolSeasonId(schoolSeasonID);
			}
			if (schoolYearID != -1) {
				SchoolYear schoolYear = this.getSchoolYear(new Integer(schoolYearID));
				if (schoolYear != null) {
					try {
						schoolClass.addSchoolYear(schoolYear);
					} catch (IDOAddRelationshipException e) {
						e.printStackTrace();
					}
				}
			}
			if (teacherID != -1) {
				User teacher = null;
				try {
					teacher = getUserBusiness().getUser(teacherID);
				} catch (RemoteException e1) {
					teacher = null;
				}
				if (teacher != null) {
					try {
						schoolClass.addTeacher(teacher);
					} catch (IDOAddRelationshipException e) {
						e.printStackTrace();
					}
				}
			}
			schoolClass.setValid(true);
			schoolClass.store();
			return schoolClass;
		} catch (FinderException fe) {
			fe.printStackTrace(System.err);
			return null;
		} catch (CreateException ce) {
			ce.printStackTrace(System.err);
			return null;
		}
	}

	@Override
	public SchoolArea getSchoolArea(Object primaryKey) {
		try {
			SchoolAreaHome shome = (SchoolAreaHome) IDOLookup.getHome(SchoolArea.class);
			return shome.findByPrimaryKey(primaryKey);
		} catch (Exception ex) {
			return null;
		}
	}

	@Override
	public SchoolSubArea getSchoolSubArea(Object primaryKey) {
		try {
			SchoolSubAreaHome shome = (SchoolSubAreaHome) IDOLookup.getHome(SchoolSubArea.class);
			return shome.findByPrimaryKey(primaryKey);
		} catch (Exception ex) {
			return null;
		}
	}

	@Override
	public void removeSchoolArea(int id) {
		removeSchoolArea(new Integer(id));
	}

	@Override
	public void removeSchoolArea(Object schoolAreaPK) {
		try {
			SchoolArea area = getSchoolArea(schoolAreaPK);
			area.remove();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	@Override
	public void removeSchoolSubArea(int id) {
		try {
			SchoolSubArea subarea = getSchoolSubArea(new Integer(id));
			subarea.remove();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	@Override
	public Collection<SchoolArea> getAllSchoolAreas() {
		try {
			SchoolAreaHome shome = (SchoolAreaHome) IDOLookup.getHome(SchoolArea.class);
			return shome.getAllScoolAreas();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Collections.emptyList();
	}

	@Override
	public Collection findAllSchoolAreas(SchoolCategory category) {
		try {
			SchoolAreaHome shome = (SchoolAreaHome) IDOLookup.getHome(SchoolArea.class);
			return shome.findAllSchoolAreas(category);
		} catch (Exception ex) {
			ex.printStackTrace();
			return new java.util.Vector();
		}
	}

	@Override
	public Collection findAllSchoolSubAreas() {
		try {
			SchoolSubAreaHome shome = (SchoolSubAreaHome) IDOLookup.getHome(SchoolSubArea.class);
			return shome.findAllSchoolSubAreas();
		} catch (Exception ex) {
			ex.printStackTrace();
			return new java.util.Vector();
		}
	}

	@Override
	public void storeSchoolArea(int id, String name, String info, String city, String accountingKey, SchoolCategory category) throws java.rmi.RemoteException {
		SchoolAreaHome shome = getSchoolAreaHome();
		SchoolArea newArea;
		try {
			if (id > 0) {
				newArea = shome.findByPrimaryKey(new Integer(id));
			} else {
				newArea = shome.create();
			}
		} catch (javax.ejb.FinderException fe) {
			throw new java.rmi.RemoteException(fe.getMessage());
		} catch (javax.ejb.CreateException ce) {
			throw new java.rmi.RemoteException(ce.getMessage());
		}
		newArea.setSchoolAreaCity(city);
		newArea.setSchoolAreaInfo(info);
		newArea.setSchoolAreaName(name);
		newArea.setAccountingKey(accountingKey);
		newArea.setCategory(category);
		newArea.store();
	}

	@Override
	public void storeSchoolSubArea(int id, String name, int areaid) throws java.rmi.RemoteException {
		SchoolSubAreaHome shome = getSchoolSubAreaHome();
		SchoolSubArea newSubArea;
		try {
			if (id > 0) {
				newSubArea = shome.findByPrimaryKey(new Integer(id));
			} else {
				newSubArea = shome.create();
			}
		} catch (javax.ejb.FinderException fe) {
			throw new java.rmi.RemoteException(fe.getMessage());
		} catch (javax.ejb.CreateException ce) {
			throw new java.rmi.RemoteException(ce.getMessage());
		}
		newSubArea.setSchoolAreaId(areaid);
		newSubArea.setSchoolSubAreaName(name);
		newSubArea.store();
	}

	@Override
	public void storeSchoolDepartment(String description, String phone, int schoolID, int schDepID) throws RemoteException {
		/**
		 * @todo figure out how to implement
		 */
		SchoolDepartment schoolDepartm = null;
		try {
			schoolDepartm = getSchoolDepartmentHome().findByPrimaryKey(new Integer(schDepID));
		} catch (FinderException e) {
			try {
				schoolDepartm = getSchoolDepartmentHome().create();
			} catch (CreateException e1) {
				e1.printStackTrace();
				// return null;
			}
		}
		schoolDepartm.setDepartment(description);
		schoolDepartm.setDepartmentPhone(phone);
		schoolDepartm.setSchoolID(schoolID);
		schoolDepartm.store();
		// return storeSchoolDepartment(description, phone, schoolID);
	}

	@Override
	public SchoolUserBusiness getSchoolUserBusiness() {
		try {
			return IBOLookup.getServiceInstance(getIWApplicationContext(), SchoolUserBusiness.class);
		} catch (RemoteException e) {
			throw new IBORuntimeException(e);
		}
	}

	@Override
	public boolean hasSchoolRelationToYear(School school, SchoolYear schoolYear) {
		try {
			int relations = getSchoolHome().getNumberOfRelations(school, schoolYear);
			if (relations > 0) {
				return true;
			}
			return false;
		} catch (IDOException ie) {
			return false;
		}
	}

	@Override
	public boolean hasAfterSchoolActivities(int schoolID) {
		try {
			int types = getSchoolHome().getNumberOfFreetimeTypes(schoolID);
			if (types > 0) {
				return true;
			}
			return false;
		} catch (IDOException ie) {
			return false;
		}
	}

	@Override
	public boolean hasSchoolPlacements(int userID) {
		try {
			int placings = getSchoolClassMemberHome().getNumberOfPlacings(userID);
			return placings > 0;
		} catch (IDOException ie) {
			ie.printStackTrace();
			return false;
		}
	}

	@Override
	public boolean hasGroupPlacement(int userID, int groupID) {
		return hasGroupPlacement(userID, groupID, false);
	}

	@Override
	public boolean hasGroupPlacement(int userID, int groupID, boolean isSubGroup) {
		try {
			if (isSubGroup) {
				return getSchoolClassMemberHome().getNumberOfSubGroupPlacings(userID, groupID) > 0;
			} else {
				return getSchoolClassMemberHome().getNumberOfPlacings(userID, groupID) > 0;
			}
		} catch (IDOException ie) {
			return false;
		}
	}

	@Override
	public String getSchoolPhone(int schoolID) {
		School school = getSchool(new Integer(schoolID));
		return school.getSchoolPhone();
	}

	/**
	 * Filters out all schools from the specified collection which do not belong
	 * the the home (default) commune.
	 *
	 * @param schools
	 *            the collection of schools
	 */

	@Override
	public Collection getHomeCommuneSchools(Collection schools) {
		ArrayList l = new ArrayList();
		try {
			CommuneHome home = (CommuneHome) IDOLookup.getHome(Commune.class);
			String defaultCommunePK = null;
			try {
				Commune defaultCommune = home.findDefaultCommune();
				defaultCommunePK = defaultCommune.getPrimaryKey().toString();
			} catch (Exception e) {
			}
			Iterator iter = schools.iterator();
			while (iter.hasNext()) {
				School school = (School) iter.next();
				boolean isHomeCommune = true;
				Object communePK = school.getCommunePK();
				if (communePK != null) {
					if (defaultCommunePK != null) {
						if (!defaultCommunePK.equals(communePK.toString())) {
							isHomeCommune = false;
						}
					}
				}
				if (isHomeCommune) {
					l.add(school);
				}
			}
		} catch (Exception e) {
		}
		return l;
	}

	@Override
	public Collection<School> findAllSchoolsByCategory(final String categoryString) {
		try {
			final SchoolCategory schoolCategory = getSchoolCategoryHome().findByPrimaryKey(categoryString);
			return getSchoolHome().findAllByCategory(schoolCategory);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Collections.emptyList();
	}

	@Override
	public String getProperty(School school, String propertyName) {
		return school.getMetaData(propertyName);
	}

	@Override
	public void setProperty(School school, String propertyName, String propertyValue) {
		school.setMetaData(propertyName, propertyValue);
		school.store();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * com.idega.block.school.business.SchoolBusiness#findAllSchoolSubAreasByArea
	 * (java.lang.String)
	 */

	@Override
	public Collection findAllSchoolSubAreasByArea(String area) {
		final Collection result = new ArrayList();
		try {
			final SchoolArea schoolArea = getSchoolAreaHome().findByPrimaryKey(area);
			result.addAll(getSchoolSubAreaHome().findAllByArea(schoolArea));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	@Override
	public SchoolClassMemberLogHome getSchoolClassMemberLogHome() {
		try {
			return (SchoolClassMemberLogHome) IDOLookup.getHome(SchoolClassMemberLog.class);
		} catch (IDOLookupException e) {
			throw new IBORuntimeException(e.getMessage());
		}
	}

	@Override
	public SchoolSubAreaHome getSchoolSubAreaHome() {
		try {
			return (SchoolSubAreaHome) IDOLookup.getHome(SchoolSubArea.class);
		} catch (IDOLookupException e) {
			throw new IBORuntimeException(e.getMessage());
		}
	}

	@Override
	public void addToSchoolClassMemberLog(SchoolClassMember member, Date endDate, User performer) throws IllegalArgumentException {
		addToSchoolClassMemberLog(member, null, null, endDate, performer);
	}

	@Override
	public void addToSchoolClassMemberLog(SchoolClassMember member, SchoolClass schoolClass, Date endDate, User performer) throws IllegalArgumentException {
		addToSchoolClassMemberLog(member, schoolClass, null, endDate, performer);
	}

	@Override
	public void addToSchoolClassMemberLog(int schoolClassMemberID, int schoolClassID, Date startDate, Date endDate, User performer) throws IllegalArgumentException {
		try {
			addToSchoolClassMemberLog(getSchoolClassMemberHome().findByPrimaryKey(new Integer(schoolClassMemberID)), getSchoolClassHome().findByPrimaryKey(new Integer(schoolClassID)), startDate, endDate, performer);
		} catch (FinderException fe) {
			log(fe);
		}
	}

	@Override
	public void addToSchoolClassMemberLog(SchoolClassMember member, SchoolClass schoolClass, Date startDate, Date endDate, User performer) throws IllegalArgumentException {
		boolean logPlacements = false;
		try {
			logPlacements = new Boolean(getIWApplicationContext().getIWMainApplication().getBundle("com.idega.block.school").getProperty(PROPERTY_NAME_USE_PLACEMENT_LOGGING, Boolean.FALSE.toString())).booleanValue();
		} catch (Exception e) {
			logPlacements = false;
		}
		if (!logPlacements) {
			return;
		}
		if (startDate == null && endDate == null) {
			throw new IllegalArgumentException("[SchoolClassMemberLog] Both start date and end date are null...");
		}
		if (startDate == null) {
			try {
				SchoolClassMemberLog log = getSchoolClassMemberLogHome().findOpenLogByUserAndSchoolClass(member, schoolClass);
				log.setUserTerminating(performer);
				log.setEndDate(endDate);
				log.store();
			} catch (FinderException fe) {
				try {
					SchoolClassMemberLog log = getSchoolClassMemberLogHome().findClosedLogByUserAndSchoolClass(member, schoolClass);
					log.setUserTerminating(performer);
					log.setEndDate(endDate);
					log.store();
				} catch (FinderException fex) {
					try {
						SchoolClassMemberLog log = getSchoolClassMemberLogHome().create();
						log.setUserPlacing(performer);
						log.setUserTerminating(performer);
						log.setSchoolClass(schoolClass);
						log.setSchoolClassMember(member);
						log.setStartDate(new IWTimestamp(member.getRegisterDate()).getDate());
						log.setEndDate(endDate);
						log.store();
					} catch (CreateException ce) {
						log(ce);
					}
				}
			}
		} else {
			SchoolClassMemberLog log = null;
			try {
				log = getSchoolClassMemberLogHome().findFutureLogByPlacementAndDate(member, startDate);
				try {
					IWTimestamp oldEndDate = new IWTimestamp(log.getStartDate());
					oldEndDate.addDays(-1);
					IWTimestamp newEndDate = new IWTimestamp(startDate);
					newEndDate.addDays(-1);
					SchoolClassMemberLog previousLog = getSchoolClassMemberLogHome().findByPlacementAndEndDate(member, oldEndDate.getDate());
					previousLog.setEndDate(newEndDate.getDate());
					previousLog.store();
				} catch (FinderException fex) {
					// No older log found
				}
			} catch (FinderException fe) {
				try {
					log = getSchoolClassMemberLogHome().create();
				} catch (CreateException ce) {
					log(ce);
				}
			}
			if (log != null) {
				log.setUserPlacing(performer);
				log.setUserTerminating(null);
				log.setSchoolClass(schoolClass);
				log.setSchoolClassMember(member);
				log.setStartDate(startDate);
				log.setEndDate(endDate);
				log.store();
			}
		}
	}

	@Override
	public boolean hasActivePlacement(int studentId, int schoolId, SchoolCategory category) {
		SchoolClassMember placement = null;
		try {
			SchoolClassMemberHome home = getSchoolClassMemberHome();
			placement = home.findNotTerminatedByStudentSchoolAndCategory(studentId, schoolId, category);
		} catch (Exception e) {
		}
		return placement != null;
	}

	@Override
	public void alignLogs(SchoolClassMember member) {
		try {
			Date placementEndDate = member.getRemovedDate() != null ? new IWTimestamp(member.getRemovedDate()).getDate() : null;
			Collection logs = getSchoolClassMemberLogHome().findAllByPlacement(member, placementEndDate);
			SchoolClassMemberLog oldLog = null;
			Date endDate = null;
			boolean first = true;
			Iterator iter = logs.iterator();
			while (iter.hasNext()) {
				SchoolClassMemberLog log = (SchoolClassMemberLog) iter.next();
				if (first) {
					oldLog = log;
					oldLog.setStartDate(new IWTimestamp(member.getRegisterDate()).getDate());
					first = false;
				} else {
					IWTimestamp startDate = new IWTimestamp(log.getStartDate());
					startDate.addDays(-1);
					endDate = startDate.getDate();
				}
				oldLog.setEndDate(endDate);
				oldLog.store();
				oldLog = log;

				if (!iter.hasNext()) {
					if (member.getRemovedDate() != null) {
						endDate = new IWTimestamp(member.getRemovedDate()).getDate();
						log.setEndDate(endDate);
					}
					log.setSchoolClass(new Integer(member.getSchoolClassId()));
					log.store();
					break;
				}
			}
			if (placementEndDate != null) {
				Collection futureLogs = getSchoolClassMemberLogHome().findAllByPlacementWithStartDateLaterThanOrEqualToDate(member, placementEndDate);
				Iterator iterator = futureLogs.iterator();
				while (iterator.hasNext()) {
					SchoolClassMemberLog element = (SchoolClassMemberLog) iterator.next();
					try {
						element.remove();
					} catch (RemoveException re) {
						re.printStackTrace();
					}
				}
			}
		} catch (FinderException fe) {
			fe.printStackTrace();
		}
	}

	@Override
	public Collection<SchoolArea> findAllSchoolAreasByType(int type) throws RemoteException {
		SchoolAreaHome schoolAreaHome = (SchoolAreaHome) IDOLookup.getHome(SchoolArea.class);
		try {
			return schoolAreaHome.findAllSchoolAreasByType(type);
		} catch (FinderException e) {
			e.printStackTrace();
		}
		return Collections.emptyList();
	}

	@Override
	public Collection<SchoolArea> findAllSchoolAreasByTypes(Collection<SchoolType> types) {
		try {
			SchoolAreaHome shome = (SchoolAreaHome) IDOLookup.getHome(SchoolArea.class);
			return shome.findAllBySchoolTypes(types);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return Collections.emptyList();
	}
}