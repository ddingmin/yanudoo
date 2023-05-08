import './App.css';
import 'bootstrap/dist/css/bootstrap.min.css';
import { BrowserRouter, Routes, Route } from "react-router-dom";
import Main from './components/main';
import KeywordsResult from './components/keywordsResult';
import QuestionResult from './components/questionResult';
import UseKeywords from './components/useKeywords';
import UseQuestion from './components/useQuestion';
import UsePicKeywords from './components/usePicKeywords';
import Contact from './components/contact';
import Navigation from './components/navigation';
function App() {
  return (
    <div className="App">
      <Navigation/>
      <BrowserRouter>
        <Routes>
          <Route exact path="/" element={<Main />} />
          <Route exact path="/keywordsResult" element={<KeywordsResult />} />
          <Route exact path="/questionResult" element={<QuestionResult />} />
          <Route exact path="/useKeywords" element={<UseKeywords />} />
          <Route exact path="/useQuestion" element={<UseQuestion />} />
          <Route exact path="/test" element={<UseQuestion />} />
          <Route exact path="/usePicKeywords" element={<UsePicKeywords />} />
          <Route exact path="/contact" element={<Contact />} />
        </Routes>
      </BrowserRouter>
    </div>
  );
}

export default App;
