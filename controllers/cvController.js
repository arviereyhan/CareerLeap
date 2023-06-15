import connection from "../database.js";
import uploadToStorage from "../storage.js";

const cvController = async (req, res) => {
  const userId = req.user.userId;

  try {
    let cv_url = null;

    if (req.file) {
      const cv_file = req.file;
      cv_url = await uploadToStorage(cv_file);
    }

    connection.query(
      "UPDATE users SET cv_url = ? WHERE id = ?",
      [cv_url, userId],
      (error, results, fields) => {
        if (error) {
          console.log(error);
          return res.status(500).json({
            error: true,
            message: "Failed to upload CV",
          });
        }

        return res.status(200).json({
          error: false,
          message: "CV uploaded successfully",
        });
      }
    );
  } catch (error) {
    console.log(error);
    return res.status(400).json({
      error: true,
      message: "Unexpected error occurred",
    });
  }
};

export default cvController;
