import connection from "../database.js";

const jobs = (req, res) => {
  const selectJob = "SELECT * FROM jobs";
  connection.query(selectJob, (err, results) => {
    if (err) {
      console.log(err, "error");
      return res.status(500).json({
        error: true,
        message: "Error occurred while fetching job data",
      });
    }
    console.log(results, "results");
    return res.status(200).json({
      error: false,
      message: "Message successfully delivered",
      data: results,
    });
  });
};

export default jobs;
