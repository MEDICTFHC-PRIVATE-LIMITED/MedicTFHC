package com.example.cool.patient;

/**
 * Created by Udayasri on 17-05-2018.
 */

public class PatientData_DoctorTodaysAppointmentsClass {

    int dstatus,appointmentID,patientID;
    String status1,patientName,emailID,mobileNo,prescription,comments,reasonAppointments,aadharNumber,timeSlots,age;

    public PatientData_DoctorTodaysAppointmentsClass(int dstatus, String status1, int appointmentID, String patientName, String emailID, String mobileNo, int patientID, String comments, String reasonAppointments,String timeSlots,String age) {

        this.dstatus=dstatus;
        this.status1=status1;
        this.appointmentID=appointmentID;
        this.patientName=patientName;
        this.emailID=emailID;
        this.mobileNo=mobileNo;
        //this.prescription=prescription;
        this.patientID=patientID;
        this.comments=comments;
        this.reasonAppointments=reasonAppointments;
//        this.aadharNumber=aadharNumber;
        this.timeSlots=timeSlots;
        this.age=age;

    }

    public int getDstatus() {
        return dstatus;
    }

    public String getStatus1() {
        return status1;
    }

    public int getAppointmentID() {
        return appointmentID;
    }

    public String getPatientName() {
        return patientName;
    }

    public String getEmailID() {
        return emailID;
    }

    public String getMobileNo() {
        return mobileNo;
    }

//    public String getPrescription() {
//        return prescription;
//    }

    public int getPatientID() {
        return patientID;
    }

    public String getComments() {
        return comments;
    }

    public String getReasonAppointments() {
        return reasonAppointments;
    }

//    public String getAadharNumber() {
//        return aadharNumber;
//    }

    public String getTimeSlots() {
        return timeSlots;
    }

    public String getAge() {
        return age;
    }

    public void setDstatus(int dstatus) {
        this.dstatus = dstatus;
    }

    public void setStatus1(String status1) {
        this.status1 = status1;
    }

    public void setAppointmentID(int appointmentID) {
        this.appointmentID = appointmentID;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public void setEmailID(String emailID) {
        this.emailID = emailID;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

//    public void setPrescription(String prescription) {
//        this.prescription = prescription;
//    }

    public void setPatientID(int patientID) {
        this.patientID = patientID;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public void setReasonAppointments(String reasonAppointments) {
        this.reasonAppointments = reasonAppointments;
    }

//    public void setAadharNumber(String aadharNumber) {
//        this.aadharNumber = aadharNumber;
//    }

    public void setTimeSlots(String timeSlots) {
        this.timeSlots = timeSlots;
    }

    public void setAge(String age) {
        this.age = age;
    }
}
