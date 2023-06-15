import jwt from "jsonwebtoken";

const checkJwtToken = (req, res, next) => {
  const secretkey = process.env.SECRET_KEY;
  const authHeader = req.headers["authorization"];
  if (!authHeader) {
    return res.status(401).json({
      error: true,
      message: "No token provided",
    });
  }
  const [bearer, token] = authHeader.split(" ");
  if (bearer !== "Bearer" || !token) {
    return res.sendStatus(401);
  }
  jwt.verify(token, secretkey, (err, user) => {
    if (err) {
      console.log(err);
      return res.status(401).json({
        error: true,
        message: "invalid token",
      });
    }
    const { id } = user;
    req.user = { userId: id };
    next();
  });
};

export default checkJwtToken;
