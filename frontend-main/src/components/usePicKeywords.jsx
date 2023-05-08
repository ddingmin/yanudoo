import {React, useEffect, useState} from 'react';
import {Link, useNavigate} from "react-router-dom" ;
import axios from 'axios';
import Loading from './Loading.js';
import '../App.css';

const UsePicKeywords = () =>{
    const [file, setFile] = useState();
    const [answer, setAnswer] = useState(0);
    const postPickeyw = "/ImageSend/withConcept"; // 키워드 사진전송 둘다 api
    const [loading, setLoading] = useState(false);
    const navigate = useNavigate();

    let a, qeustion, qanswer;


    const onSaveFiles = (e) => {
        const target = e.currentTarget;
        const files = (target.files)[0];

        if(files === undefined) {
            return ;
        }
        setFile(files);
        setAnswer(1);
    };



    const fileUploadHandler = async () => {
        if(file !== undefined) {
            try{
                setLoading(true);
                // !!중요1. formData활용!!
                const formData = new FormData();
                formData.append('file', file);

                //console.log(file);
                //formData.append('communityWritingReqDto', commuDto);

                for (let key of formData.keys()) {
                    console.log(key);
                }
                for (let value of formData.values()) {
                    console.log(value);
                }
                
                const axiosResponse = await axios.post(postPickeyw, formData)
                .then(res=>{
                    console.log(res);
                    a = res.data.data;
                }).catch(error=>{
                    console.log(error);
                });
                    // 파일 업로드 성공!
                alert(' 완료!');
                console.log(a);
                const splitString = a.split('\n\n');
                //qeustion = splitString[1].split(' : ')[1];
                //qanswer = splitString[2].split(' : ')[1];
              
                console.log(splitString);
                setLoading(false);
                navigate(`/questionResult`,  { replace: true, state: { a: splitString, file: file} }); //결과
            } 
            catch(e) {
                console.error(e);
                alert((e).message);
            }
        }
    };

    return(
        <div className='container'>
            {loading ? <Loading /> : null}
            <div className='q-container'>
                <div className='question-container'>
                    <br/>
                    <p className='title-q'>새로운 문제를 만들어 드릴게요</p>
                    <div className='filebox'>
                        <img style={{width:"50px", height:"50px"}} src="https://cdn.icon-icons.com/icons2/1471/PNG/512/12-file_101194.png"/>
                        <br/>
                        <label htmlFor="file">
                            {answer ? <div></div> : <div className="btn-upload">파일 업로드</div>}
                        </label>
                        <input type="file" id="file" onChange={onSaveFiles}></input>
                        <br/>
                        {answer ? <button onClick={fileUploadHandler}>정답보기</button> : <div> </div>}
                    </div>
                </div>
            </div>
            <br/>
        </div>
    );
}


export default UsePicKeywords;