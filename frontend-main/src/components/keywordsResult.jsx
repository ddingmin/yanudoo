import {React, useState} from 'react';
import {Link, useLocation} from "react-router-dom" ;
import axios from 'axios';
import Loading from './Loading.js';

const KeywordsResult = () =>{
    const [elements, setElements] = useState([]);
    const location = useLocation();
    const question = location.state.q;
    const ans = location.state.ans;
    const sub = location.state.sub;
    const key = location.state.key;
    const [loading, setLoading] = useState(false);
    const [showAns, setShowAns] = useState(); // 1이면 보이기, 0이면 답안 숨기기
    const [getAns2, setgetAns2] = useState(0);
    const postVal = "/KeyWord/noImg"; // 개념문제답 둘다 api
    
    let getAns = 0;// true면 추가문제

    console.log(question);
    console.log(ans);

    
    const [answerList, setAnswerList] = useState([
        {
          pic: '',
          ans: ''
        }
      ]); //답안목록
    let objecres
    let ansList= []//답안목록

    let a, qeustion, qanswer;

    const answerHandler = () => {
        setShowAns(!showAns);
    }

    
    const getAnswer = async () => {
        setLoading(true);
    
        try{
            const body = {
                subject: sub,
                keyWord: key
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
            qeustion = arr[1].slice(0,-3);
            qanswer = arr[2];
            objecres = {
                pic: qeustion,
                ans: qanswer
            }
            ansList.push(objecres);
            console.log(objecres);
        }
        catch(e) {
            console.error(e);
            alert((e).message);
        } 

        setLoading(false);
        generationfunc1();
        getAns = getAns + 1;
    }

    const generationfunc1 =() =>{
        const newElement = <div>
        <div >
            <hr></hr>
            <div className='result-usepic-title'>
                Question.
            </div>
            <div className='result-usepic2'>
                {ansList[0].pic}
            </div>
            <br/><br/>
            <div className='result-usepic-answer2'>
                {ansList[0].ans}
            </div>
            <br/><br/><br/>
            </div>
            
        </div>;
        setElements(prevElements => [...prevElements, newElement]);
        getAns2 = 1;
        setgetAns2(1);
    };


    return (
        <div className='container'>
            {loading ? <Loading /> : null}
            <div className='q-section'>
                <div className='result-usepic-title'>
                    Question.
                </div>
                <div className='result-usepic2'>
                    {question}
                </div>
                <div className='result-usepic-answer'>
                    {showAns ? <button onClick={answerHandler}>▶ 숨기기</button> : <button onClick={answerHandler}>▼ 답안보기</button>}
                </div>
                <div className='result-usepic-answer2'>
                    {showAns ? ans : ""}
                </div>
                <div className='result-usepic-gene'>
                    <button className='inner' onClick={getAnswer}>다른 문제 만들기</button>
                </div>
                {elements}
            </div>
        </div>
    )

}

export default KeywordsResult;