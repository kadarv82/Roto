package Roto.DataBase;

import java.io.Serializable;
import java.util.Date;

public class SerializedRegistration implements Serializable{
	private String companyName;
	private String companyAddress;
	private String operator;
	private String email;
	private String salesMan;
	private String comment;
	private String key1,key2,key3;
	private String code1,code2,code3;
	private String registerID;
	private boolean isLicenceFile;
	private Date registrationFrom;
	private Date registrationTo;
	//Used only if registry and user path is not enabled
	private boolean isDemoDisabled;


	public boolean isDemoDisabled() {
		return isDemoDisabled;
	}

	public void setDemoDisabled(boolean isDemoDisabled) {
		this.isDemoDisabled = isDemoDisabled;
	}

	public boolean isLicenceFile() {
		return isLicenceFile;
	}

	public void setLicenceFile(boolean isLicenceFile) {
		this.isLicenceFile = isLicenceFile;
	}
	
	public String getRegisterID() {
		return registerID;
	}
	
	public void setRegisterID(String registerID) {
		this.registerID = registerID;
	}
	
	public String getCompanyAddress() {
		return companyAddress;
	}
	
	public void setCompanyAddress(String companyAddress) {
		this.companyAddress = companyAddress;
	}
	
	public String getOperator() {
		return operator;
	}
	
	public void setOperator(String operator) {
		this.operator = operator;
	}
	
	public String getEmail() {
		return email;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}
	
	public String getSalesMan() {
		return salesMan;
	}
	
	public void setSalesMan(String salesMan) {
		this.salesMan = salesMan;
	}
	
	public String getComment() {
		return comment;
	}
	
	public void setComment(String comment) {
		this.comment = comment;
	}
	
	public String getKey1() {
		return key1;
	}
	
	public void setKey1(String key1) {
		this.key1 = key1;
	}
	
	public String getKey2() {
		return key2;
	}
	
	public void setKey2(String key2) {
		this.key2 = key2;
	}
	
	public String getKey3() {
		return key3;
	}
	
	public void setKey3(String key3) {
		this.key3 = key3;
	}
	
	public String getCode1() {
		return code1;
	}
	
	public void setCode1(String code1) {
		this.code1 = code1;
	}
	
	public String getCode2() {
		return code2;
	}
	
	public void setCode2(String code2) {
		this.code2 = code2;
	}
	
	public String getCode3() {
		return code3;
	}
	
	public void setCode3(String code3) {
		this.code3 = code3;
	}
	
	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	
	public Date getRegistrationFrom() {
		return registrationFrom;
	}
	
	public void setRegistrationFrom(Date registrationFrom) {
		this.registrationFrom = registrationFrom;
	}
	
	public Date getRegistrationTo() {
		return registrationTo;
	}
	
	public void setRegistrationTo(Date registrationTo) {
		this.registrationTo = registrationTo;
	}

}
