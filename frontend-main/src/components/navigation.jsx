import React from 'react'
import {Button, Navbar, Container, Nav} from 'react-bootstrap'
import { useNavigate } from 'react-router-dom'
const navigation = () => {
  
  return (
    <Navbar className='navbar' collapseOnSelect expand="lg" bg="white" variant="light" fixed="top">
        <Container>
            <Navbar.Brand href="/">YA NERDOO</Navbar.Brand>
            <Navbar.Toggle aria-controls="responsive-navbar-nav" />
            <Navbar.Collapse id="responsive-navbar-nav">
            <Nav className="me-auto">
            </Nav>
            <Nav>
                <Nav.Link href="useQuestion">문제사진으로 문제 생성</Nav.Link>
                &nbsp;&nbsp;
                <Nav.Link  href="useKeywords">키워드로 문제 생성</Nav.Link>
                &nbsp;&nbsp;
                <Nav.Link  href="usePicKeywords">개념사진으로 문제 생성</Nav.Link>
                &nbsp;&nbsp;
                <Nav.Link  href="contact">면접연습</Nav.Link>
            </Nav>
            </Navbar.Collapse>
        </Container>
    </Navbar>
  )
}

export default navigation;