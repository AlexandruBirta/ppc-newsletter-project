const React = require('react');
const ReactDOM = require('react-dom');

//const client = require('./client');

class App extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            class: "",
            wiki: false,
            bacon: false,
            cats: false,
            firstName: "",
            lastName: "",
            email: "",
            msgData: "",
            msgType: "",
        }
    }

    fetchNewsletter(newsletterType) {

    }

    setMessage(errorMessage) {
        //alert("error " + errorMessage)
        if (errorMessage) {
            this.setState({
                msgData: errorMessage,
                msgType: "alert-danger"
            })
        } else {
            //alert("Subscribed");
            this.setState({
                class: " done",
                msgData: "Subscribed!",
                msgType: "alert-success"
            })
        }
    }

    handleClick(e) {
        //alert("subscribing2" + " " + this.state.firstName + " " + this.state.lastName + " " + this.state.email);
        //e.preventDefault();

        if (!this.state.wiki && !this.state.bacon && !this.state.cats) {
            this.setState({
                msgData: "Please select at least one subscription",
                msgType: "alert-danger"
            })
            return;
        }
        if (!this.state.firstName || !this.state.lastName || !this.state.email) {
            this.setState({
                msgData: "Please fill all fields",
                msgType: "alert-danger"
            })
            return;
        }
        var newsletterType;
        var errorMessage = "";
        if (this.state.wiki) {
            newsletterType = "wikipediaArticle";
            fetch(`http://localhost:8080/v1/newsletters/${newsletterType}`, {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                },
                body: JSON.stringify({
                    firstName: this.state.firstName,
                    lastName: this.state.lastName,
                    email: this.state.email,
                })
            })
                .then(res => res.json())
                .then(result => {
                    if (result.errorMessage) {
                        errorMessage = errorMessage + `\n` + result.errorMessage;
                    }
                    if (!this.state.bacon && !this.state.cat) {
                        this.setMessage(errorMessage);
                    }
                })
        }


        if (this.state.bacon) {
            newsletterType = "baconIpsum";
            fetch(`http://localhost:8080/v1/newsletters/${newsletterType}`, {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                },
                body: JSON.stringify({
                    firstName: this.state.firstName,
                    lastName: this.state.lastName,
                    email: this.state.email,
                })
            })
                .then(res => res.json())
                .then(result => {
                    if (result.errorMessage) {
                        errorMessage = errorMessage + "\n" + result.errorMessage;
                    }
                    if (!this.state.cat) {
                        this.setMessage(errorMessage);
                    }
                })
        }
        if (this.state.cats) {
            newsletterType = "catPhoto";
            fetch(`http://localhost:8080/v1/newsletters/${newsletterType}`, {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                },
                body: JSON.stringify({
                    firstName: this.state.firstName,
                    lastName: this.state.lastName,
                    email: this.state.email,
                })
            })
                .then(res => res.json())
                .then(result => {
                    if (result.errorMessage) {
                        errorMessage = errorMessage + "\n" + result.errorMessage;
                    }
                    this.setMessage(errorMessage);

                })
        }
    }

    wikiChanged(e) {
        this.setState({
            wiki: e.target.checked
        })
    }

    baconChanged(e) {
        this.setState({
            bacon: e.target.checked
        })
    }

    catsChanged(e) {
        this.setState({
            cats: e.target.checked
        })
    }

    onFirstNameChanged(e) {
        this.setState({firstName: e.target.value});
    }

    onLastNameChanged(e) {
        this.setState({lastName: e.target.value});
    }

    onEmailChanged(e) {
        this.setState({email: e.target.value});
    }

    click() {
        alert(this.state.wiki + " " + this.state.bacon + " " + this.state.cats)
    }


    render() {
        var message;
        if (this.state.msgData != "")
            message = <div className={`message alert fade show d-flex ${this.state.msgType}`}>{this.state.msgData}</div>
        var newsletter = <div className="firstContainer">
            <div>
                {/*<div className="card" style={{width:"18rem"}}>*/}
                {/*    <img className="card-img-top" src={require('/built/img/card.png')} alt="Card image cap"/>*/}
                {/*        <div className="card-body">*/}
                {/*            <p className="card-text">Some quick example text to build on the card title and make up the*/}
                {/*                bulk of the card's content.</p>*/}
                {/*        </div>*/}
                {/*</div>*/}
            </div>
            <div className="container allign-self-center">
                <div className="content container-fluid d-flex align-items-center justify-content-center">
                    {message}
                    <form className={"subscription" + this.state.class}>
                        <div className="form-group"><input className="add-firstName form-control" type="text"
                                                           placeholder="First Name"
                                                           onChange={this.onFirstNameChanged.bind(this)}/></div>
                        <div className="form-group"><input className="add-firstName form-control" type="text"
                                                           placeholder="Last Name"
                                                           onChange={this.onLastNameChanged.bind(this)}/></div>
                        <div className="form-group"><input className="add-email form-control" type="email"
                                                           placeholder="subscribe@me.now"
                                                           onChange={this.onEmailChanged.bind(this)}/></div>
                        <div className="card form-check form-switch">
                            <input className="form-check-input" type="checkbox" id="wiki" name="wiki"
                                   onChange={this.wikiChanged.bind(this)}/>
                            <label className="form-check-label" for="wiki"><h6>Wikipedia articles</h6></label>
                        </div>
                        <div className="card form-check form-switch">
                            <input className="form-check-input" type="checkbox" id="bacon" name="bacon"
                                   onChange={this.baconChanged.bind(this)}/>
                            <label className="form-check-label" htmlFor="bacon"><h6>Bacon</h6></label>
                        </div>
                        <div className="card form-check form-switch">
                            <input className="form-check-input" type="checkbox" id="cats" name="cats"
                                   onChange={this.catsChanged.bind(this)}/>
                            <label className="form-check-label" htmlFor="cats"><h6>Cat photos</h6></label>
                        </div>
                        <button className="submit-email btn btn-dark" type="button"
                                onClick={this.handleClick.bind(this)}>
                            <span className="before-submit">Subscribe</span>
                            <span className="after-submit">Thank you for subscribing!</span>
                        </button>
                    </form>
                </div>
                {/*<button onClick={this.click.bind(this)}>click</button>*/}
            </div>
        </div>;
        return newsletter;
    }
}

ReactDOM.render(
    <App/>,
    document.getElementById('react')
)