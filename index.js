import express, { json } from "express";
import bcrypt, { hash } from "bcrypt";
import jwt from "jsonwebtoken";
import connection from "./database.js";
import cors from "cors";

const app = express();
app.use(express.json());
app.use(express.urlencoded({ extended: true }));

const corsOption = {
  origin: ["http://localhost:3000"],
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
        console.log(results, "results");
        if (results.length !== 0) {
          checkPassword = bcrypt.compareSync(password, results[0].password);
          id = results[0].id;
          username = results[0].full_name;
        } else {
          return res.status(404).json({ error: "There is no such as email" });
        }
        console.log(username, "username");

        // if (Array.isArray(results) && results.length === 0) {}
        if (!email || !password) {
          return res
            .status(404)
            .json({ error: "Please enter the email and password" });
        }
        if (!checkPassword) {
          return res
            .status(404)
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

app.listen(8080, () => {
  console.log("Server is running on port 8080");
});
