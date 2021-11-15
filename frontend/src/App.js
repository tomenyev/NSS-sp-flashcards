import React, { Component } from 'react';

import Footer from './blocks/Footer.js';

import Login from './components/login.component';
import Register from './components/register.component';
import Topics from './pages/topics';
import Decks from './pages/decks';
import Cards from './pages/cards';
import Study from './pages/study';
import AuthService from './services/auth/AuthService';
import {Link, NavLink} from 'react-router-dom';

import {BrowserRouter as Router, Switch, Route} from 'react-router-dom';

import 'bootstrap/dist/css/bootstrap.min.css';
import 'react-bootstrap-table-next/dist/react-bootstrap-table2.min.css';
import NewTopic from './pages/NewTopic';
import Profile from "./pages/profile";
import Search from "./pages/search";
import PublicTopic from "./pages/publictopic";
import AdminBoard from "./pages/admin/adminboard";


class App extends Component {

  constructor(props) {
    super(props);
    this.logOut = this.logOut.bind(this);

    this.state = {
      showAdminBoard: false,
      currentUser: undefined
    };
  }

  componentDidMount() {
    const user = AuthService.getCurrentUser();

    if (user) {
      this.setState({
        currentUser: AuthService.getCurrentUser(),
        showAdminBoard: user.roles.includes("ROLE_ADMIN")
      });
    }
  }

  logOut() {
    AuthService.logout();
    this.props.history.push("/signin");
  }

  render() {

    const { currentUser, showAdminBoard } = this.state;
    return (
        <Router>
          <div className="App">

            <nav className="navbar navbar-expand navbar-dark bg-dark">
              <span className="navbar-brand">FlashCards</span>
              <div className="navbar-nav mr-auto">

                {currentUser && (
                    <li className="nav-item">
                      <NavLink exact to={"/newtopic"} className="nav-link" activeClassName="active">
                        New Topic
                      </NavLink>
                    </li>
                )}
                {currentUser && (
                    <li className="nav-item">
                      <NavLink exact  to={"/topics"} className="nav-link">
                        Topics
                      </NavLink>
                    </li>
                )}
                {currentUser && (
                    <li className="nav-item">
                      <NavLink exact  to={"/search"} className="nav-link">
                        Search
                      </NavLink>
                    </li>
                )}
                {showAdminBoard && (
                    <li className="nav-item">
                      <NavLink to={"/admin"} className="nav-link">
                        Admin Board
                      </NavLink>
                    </li>
                )}
              </div>
              {currentUser ? (
                  <div className="navbar-nav ml-auto">
                    <li className="nav-item">
                      <Link to={"/profile"} className="nav-link">
                        {currentUser.username}
                      </Link>
                    </li>
                    <li className="nav-item">
                      <a href="/signin" className="nav-link" onClick={this.logOut}>
                        LogOut
                      </a>
                    </li>
                  </div>
              ) : (
                  <div className="navbar-nav ml-auto">
                    <li className="nav-item">
                      <Link to={"/signin"} className="nav-link">
                        Login
                      </Link>
                    </li>
                    <li className="nav-item">
                      <Link to={"/signup"} className="nav-link">
                        Sign Up
                      </Link>
                    </li>
                  </div>
              )})
            </nav>

            <div className="container mt-5">
              <Switch>
                <Route path="/topics" exact component={Topics}/>
                <Route path="/cards" exact component={Cards}/>
                <Route path="/decks" exact component={Decks}/>
                <Route path="/topic/:id/decks" exact component={Decks}/>
                <Route path="/topic/:id/public" exact component={PublicTopic}/>
                <Route path="/deck/:id/cards" exact component={Cards}/>
                <Route path="/newtopic" exact component={NewTopic}/>
                <Route path="/profile" exact component={Profile}/>
                <Route path="/search" exact component={Search}/>
                <Route path={["/", "/signin"]} exact component={Login}/>
                <Route path="/signup" exact component={Register}/>
                <Route path="/admin" exact component={AdminBoard}/>
                <Route path="/study/:id" exact component={Study}/>
              </Switch>
            </div>

            <Footer/>
          </div>
        </Router>
    );
  }
}

export default App;
