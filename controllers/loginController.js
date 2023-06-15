import connection from "../database.js";
import bcrypt from "bcrypt";
import jwt from "jsonwebtoken";

const loginController = (req, res) => {
  const { email, password } = req.body;

  if (!email || !password) {
    return res
      .status(401)
      .json({ error: "Please enter the email and password" });
  }

  connection.query(
    "SELECT id, full_name, email, password FROM users WHERE email = ? ",
    [email],
    (error, results, fields) => {
      try {
        if (results.length !== 0) {
          const checkPassword = bcrypt.compareSync(
            password,
            results[0].password
          );
          const id = results[0].id;
          const username = results[0].full_name;

          if (!checkPassword) {
            return res.status(401).json({
              error: true,
              message: "Incorrect password. Please try again.",
            });
          }

          // If email and password are correct, generate and send JWT token
          const payload = { email, id };
          const secretkey = process.env.SECRET_KEY;
          const token = jwt.sign(payload, secretkey);

          return res.status(200).json({
            error: false,
            message: "Success",
            loginResult: {
              userId: id,
              name: username,
              token: token,
            },
          });
        } else {
          return res.status(401).json({
            error: true,
            message: "Incorrect email",
          });
        }
      } catch (error) {
        console.log(error, "error");
        res.status(500).json({
          error: true,
          message: "Internal server error",
        });
      }
    }
  );
};

export default loginController;
