import {React, useEffect, useState} from 'react';
import {Link, useNavigate} from "react-router-dom" ;
import axios from 'axios';
import Loading from './Loading.js';
const ALLOW_FILE_EXTENSION = "jpg,jpeg,png"; // 허용가능한 확장자 목록!
const FILE_SIZE_MAX_LIMIT = 5 * 1024 * 1024;  // 5MB
import '../App.css';

const UseQuestion = () =>{
    const [file, setFile] = useState();
    const [answer, setAnswer] = useState(0);
    const postPic = "/ImageSend/detect"; // 사진전송 문제답 둘다 api
    const testapi = "/ImageSend/justAnswer"; // 사진전송답만반응 api
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
                
                const axiosResponse = await axios.post(testapi, formData)
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

    return (
        <div className='container'>
            {loading ? <Loading /> : null}
            <div className='q-container'>
                <div className='question-container'>
                    <br/>
                    <p style={{fontSize:'25px', fontFamily:'GangwonEdu_OTFBoldA'}} className='title-q'>새로운 문제를 만들어 드릴게요</p>
                    <div className='filebox'>
                        <img style={{width:"50px", height:"50px"}} src="https://cdn.icon-icons.com/icons2/1471/PNG/512/12-file_101194.png"/>
                        <br/>
                        <label htmlFor="file">
                            {answer ? <div></div> : <div style={{ fontFamily:'GangwonEdu_OTFBoldA'}} className="btn-upload">파일 업로드</div>}
                        </label>
                        <input type="file" id="file" onChange={onSaveFiles}></input>
                        <br/>
                        {answer ? <button onClick={fileUploadHandler}>정답보기</button> : <div> </div>}
                    </div>
                </div>
            </div>
            <br/>
        </div>
    )

}


/**
 * 파일 확장자를 검사해주는 함수이다.
 * @param param
 * @returns true: 가능 확장자, false : 불가능 확장자 
 */
const fileExtensionValid = (name) =>{
    // 파일 확장자
    const extension = removeFileName(name);
  
    /**
     * 허용가능한 확장자가 있는지 확인하는 부분은 indexOf를 사용해도 괜찮고, 
     * 새롭게 나온 includes를 사용해도 괜찮고, 그밖의 다른 방법을 사용해도 좋다.
     * 성능과 취향의 따라 사용하면 될것같다.
     * 
     * indexOf의 경우
     * 허용가능한 확장자가 있을경우 
     * ALLOW_FILE_EXTENSION 상수의 해당 확장자 첫 index 위치값을 반환
     */
    if(!(ALLOW_FILE_EXTENSION.indexOf(extension) > -1) || extension === '') {
      // 해당 if문이 수행되는 조건은
      // 1. 허용하지 않은 확장자일경우
      // 2. 확장자가 없는경우이다.
      return false;
    }
    return true;
  }
  
  /**
   * 해당 함수의 기능은 .을 제거한 순수 파일 확장자를 return해준다.
   * @param originalFileName 업로드할 파일명
   * @returns .을 제거한 순수 파일 확장자(png, jpg 등)
   */
  const removeFileName = (originalFileName) => {
    // 마지막 .의 위치를 구한다
    // 마지막 .의 위치다음이 파일 확장자를 의미한다
    const lastIndex = originalFileName.lastIndexOf(".");
  
    // 파일 이름에서 .이 존재하지 않는 경우이다.
    // 이경우 파일 확장자가 존재하지 않는경우(?)를 의미한다.
    if(lastIndex < 0) {
      return "";
    }
  
    // substring을 함수를 이용해 확장자만 잘라준다
    // lastIndex의 값은 마지막 .의 위치이기 때문에 해당 위치 다음부터 끝까지 문자열을 잘라준다.
    // 문자열을 자른 후 소문자로 변경시켜 확장자 값을 반환 해준다.
    return originalFileName.substring(lastIndex+1).toLowerCase();
}


export default UseQuestion;