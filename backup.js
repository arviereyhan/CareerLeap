import express, { json } from "express";
import bcrypt, { hash } from "bcrypt";
import jwt from "jsonwebtoken";
import cors from "cors";
import session from "express-session";
import multer from "multer";

import connection from "./database.js";
import uploadToStorage from "./storage.js";

const app = express();
app.use(express.json());
app.use(express.urlencoded({ extended: true }));

const storage = multer.memoryStorage();

const upload = multer({
  storage: storage,
  dest: "/tmp/",
  limits: {
    fileSize: 5 * 1024 * 1024, // no larger than 5mb, you can change as needed.
  },
});

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

app.get("/", (req, res) => {
  res.send("Hello World");
});

app.post("/register", (req, res) => {
  console.log(req.headers, "req.headers");
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

// form data
// "nama" -> "Andika",
// "nama_file" -> 'fileblababla'

const checkJwtToken = (req, res, next) => {
  const token = req.headers["x-user-token"];
  // authorizotion bearer token
  // x-user-token
  // verify token jwt.verify
  // payload.userId
  req.user = { userId: userId };
  next();
};

app.put(
  "/profile",
  // checkJwtToken,
  upload.single("nama_file"),
  async (req, res) => {
    // req.user = { userId }
    const userId = req.session.userId;
    const { full_name, date_of_birth, phone_number, location } = req.body;

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
          }
        }
      }
    );
  }
);

app.put(
  "/cv",
  // checkJwtToken,
  upload.single("file_cv"),
  async (req, res) => {
    // req.user = { userId }
    const userId = req.session.userId;

    const cv_file = req.file;
    const cv_url = await uploadToStorage(cv_file);
    console.log(cv_url, "cv_url");

    connection.query(
      "UPDATE users SET cv_url = ? WHERE id = ?",
      [cv_url, userId],
      function (error, results, fields) {
        {
          try {
            return res.status(200).json({
              error: false,
              message: "CV Uploaded",
            });
          } catch (error) {
            console.log(error, "error");
            res.status(500).json({ error: "Internal server error" });
          }
        }
      }
    );
  }
);

app.listen(8080, () => {
  console.log("Server is running on port 8080");
});
