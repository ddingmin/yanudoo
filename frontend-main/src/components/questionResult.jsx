import {React, useState, useEffect} from 'react';
import {Link, useLocation} from "react-router-dom" ;
import axios from 'axios';
import Loading from './Loading.js';
import '../App.css';

const QuestionResult = () =>{
    const [elements, setElements] = useState([]);

    const postPic = "/ImageSend/detect"; // 사진전송 문제답 둘다 api
    const location = useLocation();
    const file = location.state.file;
    const ans = location.state.a;
    const [imageSrc, setImageSrc] = useState('');
    const [showAns, setShowAns] = useState(); // 1이면 보이기, 0이면 답안 숨기기
    const [getAns2, setgetAns2] = useState(0);
    
    let getAns = 0;// true면 추가문제
    
    const [loading, setLoading] = useState(false);

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

    const getAnswer = async () => {// 최초 다른 문제를 받아와 answerList를 초기화
        setLoading(true);
    
        try{
            const formData = new FormData();
            formData.append('file', file);

            for (let key of formData.keys()) {
                console.log(key);
            }
            for (let value of formData.values()) {
                console.log(value);
            }
            const axiosResponse = await axios.post(postPic, formData)
                    .then(res=>{
                        console.log(res);
                        a = res.data.data.choices[0].text;
                    }).catch(error=>{
                        console.log(error);
                    });
                        // 파일 업로드 성공!
                    alert(' 완료!');
                    console.log(a);
                    const splitString = a.split('\n\n');
                    qeustion = splitString[1].split(' : ')[1];
                    qanswer = splitString[2].split(' : ')[1];
                
                    console.log(qeustion);
                    console.log(qanswer);
                    objecres = {
                        pic: qeustion,
                        ans: qanswer
                    }
                    ansList.push(objecres);
                    console.log(objecres);
                    setLoading(false);
        }
        catch(e) {
            console.error(e);
            alert((e).message);
        };
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

    const getAnswer2 = () => {// 이후 다른 문제를 받아와 answerList에 append
        //api

        console.log(answerList)
        setgetAns2(1);
        console.log(getAns2)
        setAnswerList([...answerList, objecres]);
        
    }

    useEffect(() => {
        console.log(file);
        setImageSrc(URL.createObjectURL(file));
      }, []);


    
    return (
        <div className='container'>
            {loading ? <Loading /> : null}
            <div className='q-section'>
                <div className='result-usepic-title'>
                    Question.
                </div>
                <div className='result-usepic2'>
                    <img alt="img" src={imageSrc} style={{width:"75%", height:"50%"}}/>
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

export default QuestionResult;