import express, { json } from "express";
import bcrypt, { hash } from "bcrypt";
import jwt from "jsonwebtoken";
import cors from "cors";
import session from "express-session";
import multer from "multer";
import { Storage } from "@google-cloud/storage";

import connection from "./database.js";
import uploadToStorage from "./storage.js";

const app = express();
app.use(express.json());
app.use(express.urlencoded({ extended: true }));

const upload = multer({ dest: "uploads/" });

// Configure session middleware
app.use(
  session({
    secret: "your_secret_key",
    resave: false,
    saveUninitialized: true,
  })
);

const corsOption = {
  origin: ["http://localhost:3000"],
  credentials: true,
};
app.use(cors(corsOption));
//if you want in every domain then
app.use(cors());

app.get("/", (req, res) => {
  res.send("Hello World");
});

app.post("/register", (req, res) => {
  const {
    email,
    password,
    full_name,
    date_of_birth,
    phone_number,
    location,
    profile_url,
    cv_url,
  } = req.body;

  const hash = bcrypt.hashSync(password, 10);

  const query = `INSERT INTO users
            (
                email,
                password,
                full_name,
                date_of_birth,
                phone_number,
                location,
                profile_url,
                cv_url
            )
            VALUES
            (
                ?, ?, ?, ?, ?, ?, ?, ?
            )`;
  connection.query(
    query,
    [
      email,
      hash,
      full_name,
      date_of_birth,
      phone_number,
      location,
      profile_url,
      cv_url,
    ],
    function (err, data) {
      if (err) {
        console.log(err, "error");
        // some error occured
        return res.send("Error");
      } else {
        // successfully inserted into db
        return res.status(200).json({
          error: false,
          message: "User Created",
        });
      }
    }
  );
});

app.post("/login", (req, res) => {
  const { email, password } = req.body;

  connection.query(
    "SELECT id, full_name, email, password FROM users WHERE email = ? ",
    [email],
    function (error, results, fields) {
      let checkPassword, id, username;
      try {
        // if (Array.isArray(results) && results.length === 0) {}
        if (results.length !== 0) {
          checkPassword = bcrypt.compareSync(password, results[0].password);
          id = results[0].id;
          username = results[0].full_name;
          req.session.userId = id;
        } else {
          return res.status(401).json({ error: "There is no such as email" });
        }

        if (!email || !password) {
          return res
            .status(401)
            .json({ error: "Please enter the email and password" });
        }
        if (!checkPassword) {
          return res
            .status(401)
            .json({ error: "Incorrect password. Please try again." });
        }

        // If email and password are correct, generate and send JWT token
        const payload = { email };
        const secretkey = "thisissecret";
        const token = jwt.sign(payload, secretkey);
        return res.status(200).json({
          error: false,
          message: "success",
          loginResult: {
            userId: id,
            name: username,
            token: token,
          },
        });
      } catch (error) {
        console.log(error, "error");
        res.status(500).json({ error: "Internal server error" });
      }
    }
  );
});
app.put("/profile", upload.single("nama_file"), async (req, res) => {
  const userId = req.session.userId;
  const { full_name, date_of_birth, phone_number, location, cv_url } = req.body;

  const profile_file = req.file;
  const profile_url = await uploadToStorage(profile_file);

  connection.query(
    "UPDATE users SET full_name = ?, date_of_birth = ?, phone_number = ?, location = ?, profile_url = ? WHERE id = ?",
    [full_name, date_of_birth, phone_number, location, profile_url, userId],
    function (error, results, fields) {
      {
        try {
          return res.status(200).json({
            error: false,
            message: "User Updated",
          });
        } catch (error) {
          console.log(error, "error");
          res.status(500).json({ error: "Internal server error" });
        }
      }
    }
  );
});

app.listen(8080, () => {
  console.log("Server is running on port 8080");
});
