import mysql from "mysql";

const connection = mysql.createConnection({
  host: "35.240.220.130",
  user: "root",
  password: "l=OP=cG:s;3G[{oU",
  database: "careerleap",
});

connection.connect((err) => {
  if (err) {
    console.error("Error connecting to MySQL database: ", err);
  } else {
    console.log("Connected to MySQL database");
  }
});

export default connection;
