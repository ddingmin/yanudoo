import {React, useEffect, useState} from 'react';
import {Link, useNavigate} from "react-router-dom" ;
import axios from 'axios';
import Loading from './Loading.js';
import '../App.css';


// -------------------------------------
const UseKeywords = () =>{
    const navigate = useNavigate();
    const [subject, setSubject] = useState();
    const [keyword, setKeyword] = useState();
    const [loading, setLoading] = useState(false);
    const postVal = "/KeyWord/noImg"; // 키워드 전송 api
    let a, qanswer, qeustion;

    const onsubmitHandler = (e) => {
        console.log(subject);
        console.log(keyword);
    };


    const onSetSub =  (e) => { // ㅈ개념 받기
        const target = e.currentTarget;
        setSubject(target.value);
    };
    const onSetKey = (e) => {// 키워드 받기
        const target = e.currentTarget;
        setKeyword(target.value);
    };

    const keySubUploadHandler = async () =>{
        try{
            setLoading(true);
            const body = {
                subject: subject,
                keyWord: keyword
            }

            const axiosResponse = await axios.post(postVal, body)
            .then(res=>{
                console.log(res);
                a = res.data.data;
            }).catch(error=>{
                console.log(error);
            });
            console.log(a);
            let splitString = a.replace(/\n/g, "");

            let arr = splitString.split(" : ");
            console.log(arr); // "문제"
            qeustion = arr[1].slice(0,-2);
            qanswer = arr[2];
            console.log(qeustion);
            console.log(qanswer);
        }
        catch(e) {
            console.error(e);
            alert((e).message);
        } 

        setLoading(false);
        navigate(`/keywordsResult`,  { replace: true, state: { q : qeustion, ans: qanswer,sub: subject, key: keyword} }); //결과
    }


    return (
        <div className='container'>
            {loading ? <Loading /> : null}
            <div className='q-container'>
                <div className='question-container'>
                    <br/>
                    <p className='title-q'>분야와 키워드를 입력하세요</p>
                    <div className='filebox'>
                        <div className='inputdiv'>
                            <div className="input-container">
                                <label className='inputlabel'>분야</label>
                                <br />
                                <input type='input' name="subject" className="text-input" onChange={onSetSub}/>
                            </div>
                            <div className="input-container">
                                <label className='inputlabel'>키워드</label>
                                <br />
                                <input type='input' name="keyword" className="text-input"  onChange={onSetKey}/>
                            </div>
                            <div className='input-container'>
                                <br />
                                <button className="btn-upload" onClick={keySubUploadHandler}>문제 생성하기</button>
                            </div>
                        </div>
                        
                    </div>
                </div>
            </div>
            <br/>
        </div>
    )

}


export default UseKeywords;