import fs from "fs";
import { Storage } from "@google-cloud/storage";
import * as dotenv from "dotenv";
import SERVICE_ACCOUNT_KEY from "./service_account.js";
dotenv.config();

export const getPublicUrl = (bucketName, fileName) =>
  `https://storage.googleapis.com/${bucketName}/${encodeURIComponent(
    fileName
  )}`;

const storage = new Storage({
  projectId: "careerleap",
  credentials: SERVICE_ACCOUNT_KEY,
});

const BUCKET_NAME = "career-leap";

const uploadToStorage = async (file) => {
  const fileName = `${Date.now()}-${file.originalname}`;
  const options = {
    destination: fileName,
  };
  try {
    let fileSource;
    if (file.path) {
      // If the file was saved to disk
      fileSource = file.path;
    } else if (file.buffer instanceof Buffer) {
      // If the file is in memory, save it to disk temporarily
      const tempFilePath = `/tmp/${fileName}`;
      fs.writeFileSync(tempFilePath, file.buffer);
      fileSource = tempFilePath;
    } else {
      throw new Error("Invalid file");
    }
    await storage.bucket(BUCKET_NAME).upload(fileSource, options);
    return getPublicUrl(BUCKET_NAME, fileName);
  } catch (error) {
    console.log(error, "error");
  }
};

export default uploadToStorage;
