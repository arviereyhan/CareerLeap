Service that we use : App Enginge, Cloud SQL, Google Storage, Cloud Build.

 Express.js-based REST API is a web service created using Express.js, a framework that simplifies web application development. 
 It allows developers to define routes for handling different types of requests and perform actions accordingly. 
 With Express.js, developers can easily add authentication, validation, and error handling to their APIs. It also supports various response formats like JSON or XML. 
 Overall, Express.js makes it easier to create simple, scalable, and secure REST APIs.
 
API Career Leap 

BASE URL : 
https://careerleap.as.r.appspot.com/

Register
URL 
/register
Method 
POST
Request Body 
email as string, unique,
password as string, must be at least 6 characters,
full_name as string
Possible error message
If the password is less then six characters : 
       error: true,
        message: "Password should be at least 6 characters long"
If the register email is already been use : 
        error: true,
        message: "Email already exists",
If there is a certain problem with email checking : 
        error: true,
        message: "Error checking email availability",
If there is unexpected error when making a new user : 
        error: true,
        message: "Unexpected Error",


Response 
{
        error: false,
        message: "User Created",
}

Login
URL 
/login
Method 
POST
Request Body 
email as string, unique
password as string, 
Possible error message
If there is no email or password : 
      .status(401)
      .json({ error: "Please enter the email and password" });
If the email is incorrect 
            error: "true",
            message: "Incorrect email",
If the password is incorrect : 
            error: "true",
            message: "Incorrect password. Please try again.",
Response 
{
	error : false, 
	message: “success”,
	loginResult: {
		userId: id,
		name: username,
		token: token,
		},
}

getProfile
URL
/profile
Method
Get
Response
{
        Error : “false”,
        userProfile : {
	“Id” : 6,
	“email” : “reyhan.arvie@gmail.com”,
	“password” : “hashpasswordyangwagakcopas”,
	“full_name” : “null”,
	“date_of_birth” : “2020-08-14T00:00:00Z”,
	“location” : “Jakata”,
	“profile_url” : null,
	“cv_url” : null,
}	
}

updateProfile
URL 
/profile
Method 
PUT
Request Body 
full_name as string,
date_of_birth as string,
phone_number as string,
location as string,
Request File
profile/file as jpg/png
Response
{
	Error : “error” 
	Success: “Register Success”
}
Upload CV
URL 
/cv
Method 
PUT,
Request File
file_cv as pdf
Possible error message 
              error: true,
              message: "Unexpected error",

Response
{
	Error : “false” 
	Success: “Upload Success”
}
Get Questions
URL 
/questions
Method 
GET,
Response
{
      error: false,
      message: "Message successfully delivered",
      data: results,
}
Get Jobs
URL 
/jobs
Method 
GET,
Response
{
      error: false,
      message: "Message successfully delivered",
      data: results,
}
Insert Job
URL 
/insertJob
Method 
POST,
Response
{
      error: false,
      message: "Job successfully choose.",
}
Insert Score
URL 
/insertScore
Method 
POST
Request Body 
question_id as int
score as int, 
Possible error message
          error: true,
          message: "Error in inserting score",
Response 
{
        error: false,
        message: "Score successfully updated",
}

GetScore
URL 
/getScore
Method 
GET
Request Body 
Perlu id dari token 
Possible error message
     error: true,
     message: "Error in retrieving score",
Response 
{
      error: false,
      message: "Score successfully retrieved",
      data: results,
}
