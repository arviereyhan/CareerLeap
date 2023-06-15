import connection from "../database.js";

const checkCourses = (req, res) => {
  const questionId = req.query.questionId;
  const userScore = req.query.userScore;
  const jobId = req.query.jobId; // New parameter

  let getCourses = `SELECT * FROM courses WHERE 1=1`;

  if (questionId) {
    getCourses += ` AND question_id = ${questionId}`;
  }

  if (userScore) {
    getCourses += ` AND course_score >= ${userScore}`;
  }

  if (jobId) {
    getCourses += ` AND job_id = ${jobId}`;
  }

  connection.query(getCourses, (err, results) => {
    console.log(results);
    if (err) {
      return res.status(500).json({
        error: true,
        message: "Error in retrieving courses",
      });
    }
    return res.status(200).json({
      error: false,
      message: "Courses successfully retrieved",
      data: results,
    });
  });
};

export default checkCourses;
