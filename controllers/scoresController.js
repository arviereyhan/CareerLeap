import connection from "../database.js";

const insertScore = (req, res) => {
  const user_id = req.user.userId;
  const { question_id, score } = req.body;
  const insertScore =
    "INSERT INTO scores (user_id, question_id, score) VALUES (?, ?, ?)";
  connection.query(
    insertScore,
    [user_id, question_id, score],
    (err, results) => {
      if (err) {
        return res.status(500).json({
          error: true,
          message: "Error in inserting score",
        });
      }
      return res.status(200).json({
        error: false,
        message: "Score successfully updated",
      });
    }
  );
};

const getScore = (req, res) => {
  const user_id = req.user.userId;
  const getScore =
    "SELECT scores.id, scores.user_id, scores.question_id, scores.score, users.id FROM scores LEFT JOIN users ON users.id = scores.user_id WHERE scores.user_id = ?;";
  connection.query(getScore, [user_id], (err, results) => {
    if (err) {
      return res.status(500).json({
        error: true,
        message: "Error in retrieving score",
      });
    }

    if (results.length === 0) {
      return res.status(200).json({
        error: false,
        message: "There is no data",
        data: null,
      });
    }

    return res.status(200).json({
      error: false,
      message: "Score successfully retrieved",
      data: results,
    });
  });
};

export { insertScore, getScore };
