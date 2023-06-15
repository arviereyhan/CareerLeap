import connection from "../database.js";
import uploadToStorage from "../storage.js";

const updateProfile = async (req, res) => {
  const userId = req.user.userId;
  const { full_name, date_of_birth, phone_number, location } = req.body;

  try {
    let profile_url = null;

    if (req.file) {
      const profile_file = req.file;
      profile_url = await uploadToStorage(profile_file);
    }

    connection.query(
      "UPDATE users SET full_name = ?, date_of_birth = ?, phone_number = ?, location = ?, profile_url = ? WHERE id = ?",
      [full_name, date_of_birth, phone_number, location, profile_url, userId],
      (error, results, fields) => {
        if (error) {
          console.log(error);
          return res.status(500).json({
            error: true,
            message: "Failed to update user profile",
          });
        }
        return res.status(200).json({
          error: false,
          message: "User profile updated successfully",
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

const getProfile = (req, res) => {
  const userId = req.user.userId;
  connection.query(
    "SELECT * FROM users WHERE id = ?",
    [userId],
    (error, results, fields) => {
      if (error) {
        console.log(error);
        return res.status(500).json({
          error: true,
          message: "Failed to retrieve user profile",
        });
      }

      if (results.length === 0) {
        return res.status(404).json({
          error: true,
          message: "User profile not found",
        });
      }

      const userProfile = results[0]; // Assuming the query returns a single profile

      return res.status(200).json({
        error: false,
        message: "User profile retrieved successfully",
        userProfile: userProfile,
      });
    }
  );
};

const insertJob = (req, res) => {
  const userId = req.user.userId;
  const { job_id } = req.body;
  const insertJob = "UPDATE users SET job_id = ? WHERE id = ?";

  connection.query(insertJob, [job_id, userId], (err, results) => {
    if (err) {
      console.log(err, "error");
      return res.status(500).json({
        error: true,
        message: "Error when inserting Job",
      });
    }
    return res.status(200).json({
      error: false,
      message: "Job succesfully choose.",
    });
  });
};

export { updateProfile, getProfile, insertJob };
