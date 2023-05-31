import { Storage } from "@google-cloud/storage";
import * as dotenv from "dotenv";
dotenv.config();

export const getPublicUrl = (bucketName, fileName) =>
  `https://storage.googleapis.com/${bucketName}/${encodeURIComponent(
    fileName
  )}`;

const storage = new Storage({
  projectId: "careerleap",
  credentials: JSON.parse(process.env.SERVICE_ACCOUNT_KEY),
});

const BUCKET_NAME = "career-leap";

const uploadToStorage = async (file) => {
  console.log(file, "file");
  const fileName = `${Date.now()}-${file.originalname}`;
  const options = {
    destination: fileName,
  };
  try {
    await storage.bucket(BUCKET_NAME).upload(file.path, options);
    return getPublicUrl(BUCKET_NAME, fileName);
  } catch (error) {
    console.log(error, "error");
  }
};

export default uploadToStorage;
