import React from 'react';
import { Link } from 'react-router-dom';
import '../App.css';
import { Animator, ScrollContainer, ScrollPage, batch, Fade, FadeIn, FadeOut, Move, MoveIn, MoveOut, Sticky, StickyIn, StickyOut, Zoom, ZoomIn, ZoomOut } from "react-scroll-motion";
import {Button } from 'react-bootstrap'

const ZoomInScrollOut = batch(StickyIn(), FadeIn(), ZoomIn());
const FadeUp = batch(Fade(), Move(), Sticky());

const Main = () =>{
    return (
        <div className='main-container'>
            <div className='scroll-container'>
                <ScrollContainer>
                    <ScrollPage>
                        <Animator animation={batch(Fade(), Sticky(), MoveOut(0, -200))}>
                        <span style={{ fontSize: "40px" ,fontFamily:"GangwonEdu_OTFBoldA"}}>문제를 풀고 싶어도 풀어볼 문제가 없지 않았나요?</span>
                        </Animator>
                    </ScrollPage>
                    <ScrollPage>
                        <Animator animation={ZoomInScrollOut}>
                        <span style={{ fontSize: "50px" ,fontFamily:"GangwonEdu_OTFBoldA"}}>이제, 공부도 똑똑하게</span>
                        </Animator>
                    </ScrollPage>
                    <ScrollPage>
                        <Animator animation={FadeUp}>
                        <span style={{ fontSize: "40px" ,fontFamily:"GangwonEdu_OTFBoldA"}}>효율적인 공부를 시작하세요!</span>
                        </Animator>
                    </ScrollPage>
                </ScrollContainer>
            </div>
            <div className='button-container'>
            <Button href="useQuestion" variant="outline-primary">문제사진으로 문제 생성</Button>{' '}
                &nbsp;&nbsp;
                {/* <Button href="useQuestion" variant="outline-primary">사진으로 문제 생성</Button>{' '} */}
                <Button href="useKeywords" variant="outline-primary">키워드로 문제 생성</Button>{' '}
                &nbsp;&nbsp;
                <Button href="usePicKeywords" variant="outline-primary">개념사진으로 문제 생성</Button>{' '}
                &nbsp;&nbsp;
                <Button href="contact" variant="outline-secondary">면접연습</Button>{' '}

            </div>
        </div>
    
    )

}

export default Main;

