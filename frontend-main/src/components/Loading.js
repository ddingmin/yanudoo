// Loading.js
import React from 'react';
import Spinner from './Spinner.gif';
import '../App.css';


export default () => {
  return (
    <div className='spinner-b'>
      <div className='LoadingText'>잠시만 기다려 주세요.</div>
      <img src={Spinner} alt="로딩중" width="5%" />
    </div>
  );
};