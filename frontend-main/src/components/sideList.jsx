import { React, useState, useEffect } from 'react';
import axios from 'axios';
import '../App.css';

const SideList = () => {
    const getListK = '/getList/KtoQList';
    const getListQ = '/getList/QtoQList';
    const [elements, setElements] = useState([]);

    const generatePage = (ListK, ListQ) => {
        const newElement = (
            <div>
                최근 문제 생성
                {ListK.slice(0, 5).map((item, index) => (
                    <div className='content-side' key={index}>
                        <p>{item.answer.slice(0, 30)+ "..."}</p>
                    </div>
                ))}
                {ListQ.slice(0, 5).map((item, index) => (
                    <div className='content-side' key={index}>
                        <p>{item.answer.slice(0, 30)+ "..."}</p>
                    </div>
                ))}
            </div>
        );
        setElements((prevElements) => [...prevElements, newElement]);
    };

    useEffect(() => {
        const fetchData = async () => {
            try {
                const res1 = await axios.get(getListK);
                const res2 = await axios.get(getListQ);
                console.log(res1)
                console.log(res2)
                const ListK = res1.data.data.map((item) => ({
                    question: item.generated_question,
                    answer: item.generated_answer,
                }));

                const ListQ = res2.data.data.map((item) => ({
                    question: item.generated_question,
                    answer: item.generated_answer,
                }));

                for (let i = 0; i <10;  i++) {
                    ListK[i].question = ListK[i].question.length > 10 ? ListK[i].question.slice(0, 5) + '…' : ListK[i].question;
                    ListQ[i].question = ListQ[i].question.length > 10 ? ListQ[i].question.slice(0, 5) + '…' : ListQ[i].question;
                }

                generatePage(ListK, ListQ);
            } catch (error) {
                console.log(error);
            }
        };

        fetchData();
    }, []);

    return <div className='sidebar-container'>{elements}</div>;
};

export default SideList;
