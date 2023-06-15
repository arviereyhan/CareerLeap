import express, { json } from "express";
import cors from "cors";
import multer from "multer";
import * as dotenv from "dotenv";
dotenv.config();
//import function
import checkJwtToken from "./middleware/checkJwtToken.js";
import loginController from "./controllers/loginController.js";
import registerController from "./controllers/registerController.js";
import {
  updateProfile,
  getProfile,
  insertJob,
} from "./controllers/profileController.js";
import cvController from "./controllers/cvController.js";
import jobs from "./controllers/perkerjaanController.js";
import questions from "./controllers/questionsController.js";
import { insertScore, getScore } from "./controllers/scoresController.js";
import checkCourses from "./controllers/coursesController.js";

//initialise express with json and urlencoded
const app = express();
app.use(express.json());
app.use(express.urlencoded({ extended: true }));

//initialise mutler
const storage = multer.memoryStorage();

const upload = multer({
  storage: storage,
  dest: "/tmp/",
  limits: {
    fileSize: 5 * 1024 * 1024, // no larger than 5mb, you can change as needed.
  },
});

//Cors on PORT: 3000
const corsOption = {
  origin: ["http://localhost:3000"],
  credentials: true,
};
app.use(cors(corsOption));

app.get("/", checkJwtToken, (req, res) => {
  res.send("Hello World");
});
app.get("/profile", checkJwtToken, getProfile);
app.get("/jobs", jobs);
app.get("/questions", questions);
app.get("/getScore", checkJwtToken, getScore);
app.get("/getCourses", checkCourses);
app.post("/register", registerController);
app.post("/login", loginController);
app.post("/insertJob", checkJwtToken, insertJob);
app.post("/insertScore", checkJwtToken, insertScore);
app.put(
  "/profile",
  checkJwtToken,
  upload.single("file_profile"),
  updateProfile
);
app.put("/cv", checkJwtToken, upload.single("file_cv"), cvController);

app.listen(8080, () => {
  console.log("Server is running on port 8080");
});
