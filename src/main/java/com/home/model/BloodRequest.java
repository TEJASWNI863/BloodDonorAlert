package com.home.model;



public class BloodRequest {
    private Long id;
    private String patientName;
    private String requiredBloodType;
    private Integer patientAge;
    private String urgencyLevel;
    private String hospital;
    private String hospitalCity;
    private String hospitalState;
    private String contactPerson;
    private String contactPhone;
    private String contactEmail;
   
    // Constructors
    public BloodRequest() {}

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public String getRequiredBloodType() {
        return requiredBloodType;
    }

    public void setRequiredBloodType(String requiredBloodType) {
        this.requiredBloodType = requiredBloodType;
    }

    public Integer getPatientAge() {
        return patientAge;
    }

    public void setPatientAge(Integer patientAge) {
        this.patientAge = patientAge;
    }

    public String getUrgencyLevel() {
        return urgencyLevel;
    }

    public void setUrgencyLevel(String urgencyLevel) {
        this.urgencyLevel = urgencyLevel;
    }

    public String getHospital() {
        return hospital;
    }

    public void setHospital(String hospital) {
        this.hospital = hospital;
    }

    

    public String getContactPerson() {
        return contactPerson;
    }

    public void setContactPerson(String contactPerson) {
        this.contactPerson = contactPerson;
    }

    public String getContactPhone() {
        return contactPhone;
    }

    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
    }

    public String getContactEmail() {
        return contactEmail;
    }

    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }

    

    @Override
    public String toString() {
        return "BloodRequest{" +
                "id=" + id +
                ", patientName='" + patientName + '\'' +
                ", requiredBloodType='" + requiredBloodType + '\'' +
                ", urgencyLevel='" + urgencyLevel + '\'' +
                ", hospital='" + hospital + '\'' +
                '}';
    }

	public String getHospitalCity() {
		return hospitalCity;
	}

	public void setHospitalCity(String hospitalCity) {
		this.hospitalCity = hospitalCity;
	}

	public String getHospitalState() {
		return hospitalState;
	}

	public void setHospitalState(String hospitalState) {
		this.hospitalState = hospitalState;
	}
}