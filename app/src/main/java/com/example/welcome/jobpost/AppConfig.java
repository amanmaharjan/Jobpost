package com.example.welcome.jobpost;

/**
 * Created by Aman on 5/30/2017.
 */

public class AppConfig {
 //public static String ip="192.168.0.112";
 public static String ip="192.168.1.2";
   // public static String ip="192.168.0.4";

    //for Welcom page
    // Server user login url
    public static String URL_Jobseeker_LOGIN = "http://"+ip+"/androidlogin/jobseeker/login.php";

    // Server user login url
    public static String URL_COMPANY_LOGIN = "http://"+ip+"/androidlogin/company/login.php";

    // Server user register url for Job seeker ***SignupJobSeekr***
    public static String URL_Jobseeker_REGISTER = "http://"+ip+"/androidlogin/jobseeker/register.php";

    // Server user register url for Company ***SignupCompany***
    public static String URL_Company_REGISTER  = "http://"+ip+"/androidlogin/company/register.php";

    // Server user re register url for compay **EDitCompanyProfile*****@
    public static String URL_Edit_Company = "http://"+ip+"/androidlogin/update_company/update.php";

    // Server user re register url for Jobseeker **EDitProfile*****
    public static String URL_Edit_Jobseeker = "http://"+ip+"/androidlogin/update/update.php";

    //for upload and download cv for Job seeker **CV****
    public static final String UPLOAD_URL = "http://"+ip+"/androidlogin/upload/uploadPdf.php";
    public static final String PDF_FETCH_URL ="http://"+ip+"/androidlogin/upload/downloadpdf.php";

    //for uploading the company jobs ****CompanyJobs****
    public static String URL_CompanyJobs_REGISTER = "http://"+ip+"/androidlogin/companyjob/companyjobs.php";

    //for viewing the JOBS in JObsekker ****JOBs*****
    public static String URL_Jobseeker_Jobs ="http://"+ip+"/androidlogin/companyjob/companyjobs.php";

    //server Jobseeker Education
    public static String URL_Jobseeker_Education= "http://"+ip+"/androidlogin/education/JobseekerEducation.php";

    //server Jobseeker Experience
    public static String URL_Jobseeker_Experience= "http://"+ip+"/androidlogin/experience/jobseekerExperience.php";

    //server Jobseeker Experience UPDATE
    public static String URL_Jobseeker_Experience_Update= "http://"+ip+"/androidlogin/experience/updateExperince.php";
    //server Jobseeker Education UPDATE
    public static String URL_Jobseeker_Education_Update= "http://"+ip+"/androidlogin/education/updateEducation.php";

    //link for uploading the images
    public static String UPLOAD_IMAGE_URL = "http://"+ip+"/androidlogin/upload/uploadImage.php";

    //server SelectedCandidate
    public static String URL_Company_SelectedCandidate= "http://"+ip+"/androidlogin/SelectedCandidate/SelectedCandidate.php";

    //server DELETE EDUCATION DATA
    public static String URL_Jobseeker_Education_Delete= "http://"+ip+"/androidlogin/education/delete.php";

    //server Jobseeker Experience DELETE
    public static String URL_Jobseeker_Experience_Delete= "http://"+ip+"/androidlogin/experience/delete.php";

    //server SelectedCandidate
    public static String URL_UPLOAD_LOGO= "http://"+ip+"/androidlogin/upload/uploadLogo.php";

    //to upload logo
    public static String URL_Company_AppliedCandidate= "http://"+ip+"/androidlogin/Join/tableJoin.php";

  //to downaod_company logo logo
  public static String URL_Download_Logo= "http://"+ip+"/androidlogin/upload/downloadImage.php";

 // /to downaod_JobSeker Profile Picture
  public static String URL_Download_Profile= "http://"+ip+"/androidlogin/upload/downloadImageJobseeker.php";

    // Jobseeker reset Password url
    public static String URL_Jobseeker_RESET= "http://"+ip+"/androidlogin/jobseeker/resetPassword.php";

    // Company reset Password url
    public static String URL_Company_RESET= "http://"+ip+"/androidlogin/company/resetPassword.php";

    //For Training MODULE

    public static String URL_Jobseeker_Training= "http://"+ip+"/androidlogin/training/JobseekerTraining.php";

    public static String URL_Jobseeker_Training_Update= "http://"+ip+"/androidlogin/training/updateTraining.php";

    public static String URL_Jobseeker_Training_Delete= "http://"+ip+"/androidlogin/training/delete.php";




}
