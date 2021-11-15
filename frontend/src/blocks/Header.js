import React, {Component} from 'react';
import {Navbar} from 'react-bootstrap';
import {Nav} from 'react-bootstrap';
import {Form} from 'react-bootstrap';
import {FormControl} from 'react-bootstrap';
import {Button} from 'react-bootstrap';

import {createBrowserHistory} from 'history';

import {
  BrowserRouter as Router,
  Switch,
  Route,
  Link
} from "react-router-dom";

export const history = createBrowserHistory({forceRefresh:true});

export default class Header extends Component {
  render() {
    return (
      <Router>
      <header>
      <Navbar bg="light" expand="lg">
      <Navbar.Brand href="#home">FlashCards</Navbar.Brand>
      <Navbar.Toggle aria-controls="basic-navbar-nav"/>
      <Navbar.Collapse id="basic-navbar-nav">
        <Nav className="mr-auto">
          <Nav.Link to="/newTopic">New Topic</Nav.Link>
          <Nav.Link to="/">My Topics</Nav.Link>
        </Nav>
        <Form inline="inline">
          <FormControl type="text" placeholder="Search" className="mr-sm-2"/>
          <Button variant="outline-success">Search</Button>
        </Form>
      </Navbar.Collapse>
    </Navbar>
    </header>
    </Router>
  )
  }
}
