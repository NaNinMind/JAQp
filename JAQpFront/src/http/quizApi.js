import { apiHost } from "./apiIndex";
const quizBase = "/api/quiz/";

export const GetOwnedByMe = async (_token) => {
  return apiHost.get(quizBase + "get_owned", {
    headers: {
      Authorization: "Bearer " + _token,
    },
  });
};

export const GetOwned = async (_id) => {
  return apiHost.get(quizBase + "get_owned/"+_id);
};

export const CreateNewQuiz = async (_token) => {
  var formdata = new FormData();
  return apiHost.post(quizBase + "create", formdata, {
    headers: {
      Authorization: "Bearer " + _token,
    },
  });
};

export const EditQuizWOImage = async (
  _token,
  _id,
  _tags,
  _description,
  _name
) => {
  var formdata = new FormData();
  formdata.append("tags", _tags);
  formdata.append("description", _description);
  formdata.append("name", _name);
  return apiHost.put(quizBase + "change_wo_image/" + _id, formdata, {
    headers: { Authorization: "Bearer " + _token },
  });
};

export const GetQuizById = async (_id) => {
  return apiHost.get(quizBase + _id);
};

export const ToggleVisabylity = async (_token, _id) => {
  return apiHost.post(quizBase + "change_public/" + _id, null, {
    headers: { Authorization: "Bearer " + _token },
  });
};

export const EditQuiz = async (
  _token,
  _id,
  _tags,
  _description,
  _name,
  _image,
  _imageName
) => {
  var formdata = new FormData();
  formdata.append("tags", _tags);
  formdata.append("description", _description);
  formdata.append("name", _name);
  if (_image) {
    formdata.append("thumbnail", _image, _imageName);
  }
  return apiHost.put(quizBase + "change/" + _id, formdata, {
    headers: { Authorization: "Bearer " + _token },
  });
};

export const DeleteQuiz = async (_token, _id) => {
  
  return apiHost.delete(quizBase + "remove/" + _id, {
    headers: { Authorization: "Bearer " + _token },
  });
};

export const GetQuestions = async (_id) => {
  return apiHost.get(quizBase + "get_questions/" + _id);
};
