const express = require("express");
const socket = require("socket.io");
const app = express();
var PORT = process.env.PORT || 3000;
const server = app.listen(PORT);
app.use(express.static("public"));
console.log("Server is running");
const io = socket(server);

let messageList = [];

io.on("connection", (socket) => {
  console.log("New socket connection: " + socket.id);

  socket.on("connectedUser", () => {
    io.emit("allMessages", messageList);
  });

  socket.on("sendMessage", (username, message, photo) => {
    messageList.push({
      username,
      message,
      photo,
    });

    io.emit("userMessage", {
      username,
      message,
      photo,
    });
  });
});
