import { apiHost } from "./apiIndex";
const answerBase = "/api/answer/"

export const AddAnswer = async (_token, _id) =>
{
    const formdata = new FormData();
    formdata.append("is_right", "false");
    formdata.append("question_id", _id);

    return apiHost.post(answerBase + "add", formdata, {
        headers: {
          Authorization: "Bearer " + _token,
        },
      });

}

export const GetAnswer = async (_id) =>
{
    return apiHost.get(answerBase+_id);
}

export const DeleteAnswer = async (_token, _id) =>
  {
    return apiHost.delete(answerBase+"remove/"+_id, {
      headers: {Authorization: 'Bearer '+ _token}
    })
  }

  
export const ChangeAnswerWOImage = async (_token, _id, _description, _isRight) =>
  {
    var formdata = new FormData()
    formdata.append("is_right", _isRight);
    formdata.append("content", _description);
    return apiHost.put(answerBase+"change_wo_image/"+_id, formdata, {
      headers: {Authorization: 'Bearer '+ _token}
    })

  }

export const ChangeAnswer = async (_token, _id, _description, _isRight, _image, _imageName) =>
    {
      var formdata = new FormData()
      formdata.append("is_right", _isRight);
      formdata.append("content", _description);
      if (_image) {
        formdata.append("image", _image, _imageName)  
      }
      return apiHost.put(answerBase+"change/"+_id, formdata, {
        headers: {Authorization: 'Bearer '+ _token}
      })
  
    }