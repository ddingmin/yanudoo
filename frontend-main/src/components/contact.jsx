import React, { useState, useRef } from 'react';
import axios from 'axios';
import Loading from './Loading.js';
import '../App.css';

const Contact = () => {
  const [loading, setLoading] = useState(false);
  const [userInput, setUserInput] = useState('');
  const [interviewerInput, setInterviewerInput] = useState('안녕하세요, 면접을 보고싶다면 말해주세요.');
  const [tmp, setTmp] = useState('부드러운');
  const [start, setStart] = useState(1);
  const apiURL = '/Interview/makeChat';

  const handleUserInputChange = (e) => {
    setUserInput(e.target.value);
  };

  const handleTmpChange = (e) => {
    setTmp(e.target.value);
  };

  const submitUserInput = async () => {
    try {
        setLoading(true);
        const response = await axios.post(apiURL, {
            question: userInput,
            tmp,
        });
      // API에서 반환하는 응답에 맞게 변수명을 변경해주세요.
      setInterviewerInput(response.data.data);
      setUserInput('');
    } catch (error) {
      console.error(error);
    }
    setLoading(false);
    setStart(0);
  };

  return (
    <div className='container-contact'>
        {loading ? <Loading /> : null}
      {start ? <h3 className='title-contact'>면접관의 태도 설정</h3> : null}
      {start ? <input type="text" value={tmp} onChange={handleTmpChange} /> : null}
      
      <h3 className='interviewer'>면접관: {interviewerInput}</h3>
      <h3 className='user'>나: {userInput}</h3>
      <input className='input-contact' type="text" value={userInput} onChange={handleUserInputChange} />
      <button onClick={submitUserInput}>Send</button>
    </div>
  );
};

export default Contact;
