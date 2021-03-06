package com.idega.block.school.presentation;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Iterator;

import com.idega.block.school.business.SchoolBusiness;
import com.idega.block.school.data.SchoolType;
import com.idega.business.IBOLookup;
import com.idega.presentation.Block;
import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.text.Link;
import com.idega.presentation.text.Text;
import com.idega.util.PresentationUtil;

/**
 * @author gimmi
 */

public class SchoolTypeSelector extends Block {
	public static final String IW_BUNDLE_IDENTIFIER = "com.idega.block.school";

	public static String PARAMETER_SCHOOL_TYPE_ID = "schsel_schtyid";
	private int _schoolTypeId = -1;

	private boolean _horizontal = true;
	private String _fontStyle;
	private String _fontColor;
	private String _selStyle;
	private String _selColor;
	private int _spaceBetween = 5;
	private String _category;
	private boolean _showOnlyFreetime = false;
	private boolean _showOnlyNonFreetime = false;
	
	public void main(IWContext iwc) throws RemoteException {
		PresentationUtil.addStyleSheetToHeader(iwc, getBundle(iwc).getVirtualPathWithFileNameString("style/school.css"));
		init(iwc);

		drawList(iwc);
	}

	private void init(IWContext iwc) {
		if (iwc.isParameterSet(PARAMETER_SCHOOL_TYPE_ID)) {
			try {
				this._schoolTypeId = Integer.parseInt(iwc.getParameter(PARAMETER_SCHOOL_TYPE_ID));
			}
			catch (NumberFormatException n) {
				n.printStackTrace(System.err);
			}
		}
	}

	public String getBundleIdentifier() {
		return IW_BUNDLE_IDENTIFIER;
	}
	private void drawList(IWContext iwc) throws RemoteException {
		SchoolBusiness stb = (SchoolBusiness) IBOLookup.getServiceInstance(iwc, SchoolBusiness.class);
		Collection coll;
		if (this._category != null) {
			coll = stb.findAllSchoolTypesInCategory(this._category);
			
		}
		else {
			coll = stb.findAllSchoolTypes();
		}

		if (this._showOnlyFreetime) {
			coll = stb.findAllSchoolTypesInCategoryFreeTime(this._category);
		}
		else if (this._showOnlyNonFreetime) {
			coll = stb.findAllSchoolTypesInCategory(this._category, false);
		}
		
		if (coll != null) {
			SchoolType sType;
			Table table = new Table();
			int sTypeId;
			table.setCellpaddingAndCellspacing(0);
			int row = 1;
			int col = 1;

			Iterator iter = coll.iterator();
			while (iter.hasNext()) {
				sType = (SchoolType) iter.next();
				sTypeId = ((Integer) sType.getPrimaryKey()).intValue();
				
				
				if (sTypeId == this._schoolTypeId) {
					table.add(getText(sType.getName(), true), col, row);
				}
				else {
					table.add(getLink(sType.getName(), sType.getPrimaryKey().toString()), col, row);
				}
				
				if (this._spaceBetween > 0 && iter.hasNext()) {
					if (this._horizontal) {
						table.setWidth(++col, row, String.valueOf(this._spaceBetween));
					}
					else {
						table.setHeight(col, ++row, String.valueOf(this._spaceBetween));	
					}
				}

				if (this._horizontal) {
					col++;		
				}
				else {
					row++;	
				}
			}
			add(table);
		}
		else {
			add("No types found");
		}

	}

	private Link getLink(String content, String primaryKey) {
		Link link = new Link(getText(content, false));
		link.addParameter(PARAMETER_SCHOOL_TYPE_ID, primaryKey);
		return link;
	}

	private Text getText(String content, boolean selected) {
		Text text = new Text(content);
		if (selected) {
			if (this._selColor != null) {
				text.setFontColor(this._selColor);
			}
			if (this._selStyle != null) {
				text.setFontStyle(this._selStyle);
			}
		}
		else {
			if (this._fontColor != null) {
				text.setFontColor(this._fontColor);
			}
			if (this._fontStyle != null) {
				text.setFontStyle(this._fontStyle);
			}
		}
		return text;
	}

	/** Setters */

	public void setHorizontalView(boolean horizontal) {
		this._horizontal = horizontal;
	}

	public void setVerticalView(boolean vertical) {
		this._horizontal = !vertical;
	}

	public void setSpaceBetween(int spaceBetween) {
		this._spaceBetween = spaceBetween;
	}

	public void setFontStyle(String style) {
		this._fontStyle = style;
	}

	public void setFontColor(String color) {
		this._fontColor = color;
	}

	public void setSelectedFontStyle(String style) {
		this._selStyle = style;
	}

	public void setSelectedFontColor(String color) {
		this._selColor = color;
	}

	public void setSchoolCategory(String category) {
		this._category = category;
	}
	
	public void setShowOnlyFreetimeType(boolean showOnlyFreetime) {
		this._showOnlyFreetime = showOnlyFreetime;
	}
	
	public void setShowOnlyNonFreetimeType(boolean showOnlyNonFreetime) {
		this._showOnlyNonFreetime = showOnlyNonFreetime;
	}
}
