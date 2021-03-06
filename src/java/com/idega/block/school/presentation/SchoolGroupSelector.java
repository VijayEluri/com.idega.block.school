/*
 * $Id: SchoolGroupSelector.java,v 1.6 2008/09/26 09:35:09 alexis Exp $
 * Created on 25.11.2004
 *
 * Copyright (C) 2004 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package com.idega.block.school.presentation;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Iterator;

import javax.ejb.EJBException;
import javax.ejb.FinderException;

import com.idega.block.school.data.School;
import com.idega.block.school.data.SchoolCategory;
import com.idega.block.school.data.SchoolClass;
import com.idega.block.school.data.SchoolClassHome;
import com.idega.block.school.data.SchoolHome;
import com.idega.block.school.data.SchoolType;
import com.idega.block.school.data.SchoolTypeHome;
import com.idega.block.web2.business.Web2Business;
import com.idega.business.IBOLookup;
import com.idega.data.IDOLookup;
import com.idega.data.IDOLookupException;
import com.idega.data.IDORelationshipException;
import com.idega.presentation.IWContext;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.presentation.ui.InterfaceObject;
import com.idega.presentation.ui.SelectOption;
import com.idega.presentation.ui.util.SelectorUtility;
import com.idega.util.CoreConstants;
import com.idega.util.PresentationUtil;

/**
 * 
 *  Last modified: $Date: 2008/09/26 09:35:09 $ by $Author: alexis $
 * 
 * @author <a href="mailto:aron@idega.com">aron</a>
 * @version $Revision: 1.6 $
 */
public class SchoolGroupSelector extends InterfaceObject {
    
    public static final String PARAMETER_ACTION = "p_act";
	public static final String ACTION_UPDATE_SCHOOLS = "a_uc";
	public static final String ACTION_UPDATE_GROUPS = "a_upc";
	
	public static final String PARAMETER_TYPE_ID = "pcd_";
	
	private static final String BLOCK_SCHOOL_JS = "/javascript/block_school.js";
    
    private String specifiedTypeID = null;
	private String specifiedSchoolID =  null;
	private String specifiedGroupID = null;
	
	private String parTypeID = null;
	private String parSchoolID = null;
	private String parGroupID = null;
    
    private DropdownMenu typeDrop = null;
	private DropdownMenu schoolDrop = null;
	private DropdownMenu groupDrop = null;
	
	private SchoolCategory category = null;
	private String iframeName = "tmpFrame";
	private Collection types = null;
	
	public SchoolGroupSelector() {
		this("sipc", "sipci", "sipz");
	}
	
	public SchoolGroupSelector(String typeParameterName, String schoolParameterName, String groupParameterName) {
		this.parTypeID = typeParameterName;
		this.parSchoolID = schoolParameterName;
		this.parGroupID = groupParameterName;
		
		this.iframeName = this.parTypeID + "_" + this.parSchoolID + "_" + this.parGroupID;
		
		setName(this.iframeName);
		
		this.typeDrop = new DropdownMenu(this.parTypeID);
		this.typeDrop.setId(this.parTypeID);
		this.typeDrop.setStyleClass("schoolTypeDropdown");
		this.schoolDrop = new DropdownMenu(this.parSchoolID);
		this.schoolDrop.setId(this.parSchoolID);
		this.schoolDrop.setStyleClass("schoolDropdown");
		this.groupDrop = new DropdownMenu(this.parGroupID);
		this.groupDrop.setId(this.parGroupID);
		this.groupDrop.setStyleClass("schoolGroupDropdown");
	}
	
	public Object clone() {
	    SchoolGroupSelector inp = (SchoolGroupSelector) super.clone();
		if (this.typeDrop != null) {
			inp.typeDrop = (DropdownMenu) this.typeDrop.clone();
		}
		if (this.schoolDrop != null) {
			inp.schoolDrop = (DropdownMenu) this.schoolDrop.clone();
		}
		if (this.groupDrop != null) {
			inp.groupDrop = (DropdownMenu) this.groupDrop.clone();
		}
		if (this.category != null) {
			inp.category = this.category;
		}
		if(this.types!=null){
		    inp.types = this.types;
		}
		return inp;
	}
	
	public DropdownMenu getTypeDropdown() {
		return this.typeDrop;
	}
	
	public DropdownMenu getSchoolDropdown() {
		return this.schoolDrop;
	}
	
	public DropdownMenu getGroupDropdown() {
		return this.groupDrop;
	}
	
	public void main(IWContext iwc) throws Exception {

		String usedTypeID = iwc.getParameter(this.parTypeID);
		String usedSchoolID = iwc.getParameter(this.parSchoolID);
		String usedGroupID = iwc.getParameter(this.parGroupID);
		
		if (this.specifiedTypeID != null) {
		    usedTypeID = this.specifiedTypeID;
		}
		if (this.specifiedSchoolID != null) {
		    usedSchoolID = this.specifiedSchoolID;
		}
		if (this.specifiedGroupID != null) {
			usedGroupID = this.specifiedGroupID;
		}

		SchoolTypeHome typeHome = (SchoolTypeHome) IDOLookup.getHome(SchoolType.class);
		SchoolHome schoolHome = (SchoolHome)IDOLookup.getHome(School.class);
		SchoolClassHome groupHome = (SchoolClassHome) IDOLookup.getHome(SchoolClass.class);
		if ( this.types==null && this.category != null ) {
			this.types = typeHome.findAllByCategory((String)this.category.getPrimaryKey());
		}		
		Collection schools = null;
		Collection groups = null;

		SelectorUtility su = new SelectorUtility();
		
		PresentationUtil.addJavaScriptSourceLineToHeader(iwc, getWeb2Business(iwc).getBundleURIToMootoolsLib());
		PresentationUtil.addJavaScriptSourceLineToHeader(iwc, CoreConstants.DWR_ENGINE_SCRIPT);
		PresentationUtil.addJavaScriptSourceLineToHeader(iwc, CoreConstants.DWR_UTIL_SCRIPT);
		PresentationUtil.addJavaScriptSourceLineToHeader(iwc, "/dwr/interface/SchoolBusiness.js");
		PresentationUtil.addJavaScriptSourceLineToHeader(iwc, getBundle(iwc).getResourcesVirtualPath() + BLOCK_SCHOOL_JS);
		PresentationUtil.addJavaScriptActionToBody(iwc, "window.addEvent('domready', initializeSchoolDoubleDropdowns);");
		
		this.typeDrop.addMenuElement("-1", "Select a type");
		this.typeDrop.addMenuElements(this.types);
		if (usedTypeID != null) {
		    schools = schoolHome.findAllBySchoolType(Integer.valueOf(usedTypeID).intValue());
			this.typeDrop.setSelectedElement(usedTypeID);
		}
		
		if (schools != null) {
			Iterator iter = schools.iterator();
			while (iter.hasNext()) {
				School school = (School) iter.next();
				this.schoolDrop.addMenuElement(school.getPrimaryKey().toString(), school.getName());
			}
		}
		if (schools == null) {
			this.schoolDrop.addFirstOption(new SelectOption("Select a school", "-1"));
		} 
		
		if (usedSchoolID != null) {
			groups = groupHome.findBySchoolAndCategory(Integer.valueOf(usedSchoolID).intValue(),this.category.getName());
			this.schoolDrop.setSelectedElement(usedSchoolID);
		}

		this.groupDrop = (DropdownMenu) su.getSelectorFromIDOEntities(this.groupDrop, groups, "getName");
		if (schools == null) {
			this.groupDrop.addFirstOption(new SelectOption("Select a group", "-1"));
		}
		if (usedGroupID != null) {
		    this.groupDrop.setSelectedElement(usedGroupID);
		}
				
		
		if (this.typeDrop.getParent() == null) {
			add(this.typeDrop);
		}
		if (this.schoolDrop.getParent() == null) {
			add(this.schoolDrop);
		}
		if (this.groupDrop.getParent() == null) {
			add(this.groupDrop);
		}

	}
	
	public Web2Business getWeb2Business(IWContext iwc) throws RemoteException {
		return (Web2Business) IBOLookup.getServiceInstance(iwc, Web2Business.class);
	}
	
	public void setSelectedSchool(Object schoolPK){
	    try {
            SchoolHome schoolHome = (SchoolHome) IDOLookup.getHome(School.class);
            School school = schoolHome.findByPrimaryKey(schoolPK);
            this.specifiedSchoolID = school.getPrimaryKey().toString();
            this.types = school.getSchoolTypes();
        } catch (IDOLookupException e) {
            
        } catch (EJBException e) {
           
        } catch (IDORelationshipException e) {
            
        } catch (FinderException e) {
           
        }
	}
	
	/*
	public void setSelectedPostalCode(Object schoolClassPK) throws FinderException {
		try {
			SchoolClassHome pcHome = (SchoolClassHome) IDOLookup.getHome(SchoolClass.class);
		
			SchoolClass schoolClass = pcHome.findByPrimaryKey(schoolClassPK);
			School school = schoolClass.getSchool();
			specifiedTypeID = school.getSchoolTypes();
			specifiedSchoolID = pc.getName();
			specifiedGroupID = postalCodePK.toString();
		} catch (IDOLookupException e) {
			e.printStackTrace();
		} 
	}*/

	public void setStyleClass(String styleClass) {
		this.typeDrop.setStyleClass(styleClass);
		this.schoolDrop.setStyleClass(styleClass);
		this.groupDrop.setStyleClass(styleClass);
	}
	
	public void setSchoolCategory(SchoolCategory category){
	    this.category = category;
	}

    /* (non-Javadoc)
     * @see com.idega.presentation.ui.InterfaceObject#handleKeepStatus(com.idega.presentation.IWContext)
     */
    public void handleKeepStatus(IWContext iwc) {
        // TODO Auto-generated method stub

    }

    /* (non-Javadoc)
     * @see com.idega.presentation.PresentationObject#isContainer()
     */
    public boolean isContainer() {
        // TODO Auto-generated method stub
        return false;
    }

}
