import connection from "../database.js";
import bcrypt from "bcrypt";

const registerController = (req, res) => {
  const { email, password, full_name } = req.body;

  if (!email || !password) {
    return res.status(400).json({
      error: true,
      message: "Please enter an email and password",
    });
  }

  if (password.length < 6) {
    return res.status(400).json({
      error: true,
      message: "Password should be at least 6 characters long",
    });
  }

  const emailCheckQuery = "SELECT * FROM users WHERE email = ?";
  const insertQuery = `INSERT INTO users (email, password, full_name) VALUES (?, ?, ?)`;

  connection.query(emailCheckQuery, [email], (err, results) => {
    if (err) {
      console.log(err);
      return res.status(500).json({
        error: true,
        message: "Error checking email availability",
      });
    }

    if (results.length > 0) {
      return res.status(409).json({
        error: true,
        message: "Email already exists",
      });
    }

    const hash = bcrypt.hashSync(password, 10);

    connection.query(insertQuery, [email, hash, full_name], (err, data) => {
      if (err) {
        console.log(err);
        return res.status(500).json({
          error: true,
          message: "Failed to register user",
        });
      }

      return res.status(200).json({
        error: false,
        message: "User registered successfully",
      });
    });
  });
};

export default registerController;
