import React, { Component } from "react";

import AuthService from "../services/auth/AuthService";

import Study from '../components/study.component.js';

import '../styles/study.css';


class StudyPage extends Component {

    constructor(props) {
        super(props);

        this.state = {
            currentUser: AuthService.getCurrentUser(),
            cards: [],
            isLoading: false
        }
    }

    componentDidMount() {
        if (!this.state.currentUser) {
            this.props.history.push("/signin");
        }
    }

    render() {
      return (
        <center>
            <h4>Study deck #{this.props.match.params.id}</h4>
            <Study deckId={this.props.match.params.id}/>
        </center>
      );
    }
  }

export default StudyPage;