const React = require('react');
const ReactDOM = require('react-dom');
//const client = require('./client');
import styles from "./app.css"

class App extends React.Component{
    constructor(props)
    {
        super(props);
        this.state = {
            class: "",
            wiki: false,
            bacon:false,
            cats:false,
            firstName:"",
            lastName:"",
            email:""
        }
    }
    handleClick(e)
    {
        alert("subscribing2" + " " + this.state.firstName + " " + this.state.lastName + " " + this.state.email);
        //e.preventDefault();
        var newsletterType;
        if(this.state.wiki)
            newsletterType="wikipediaArticle";
        else if(this.state.bacon)
            newsletterType="baconIpsum";
        else if(this.state.cats)
            newsletterType="catPhoto"


        fetch(`http://localhost:8080/v1/newsletters/${newsletterType}`,{
            method: "POST",
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify({
                firstName:this.state.firstName,
                lastName:this.state.lastName,
                email:this.state.email,
            })
        })
            .then(res => res.json())
            .then(result => {
                if(result.errorMessage)
                    alert(result.errorMessage);
                else{
                    alert("Subscribed");
                    this.setState({
                        class: " done",
                    })
                }

            })


    }

    wikiChanged(e)
    {
        this.setState({
            wiki: e.target.checked
        })
    }
    baconChanged(e)
    {
        this.setState({
            bacon: e.target.checked
        })
    }
    catsChanged(e)
    {
        this.setState({
            cats: e.target.checked
        })
    }
    onFirstNameChanged(e)
    {
        this.setState({firstName:e.target.value});
    }
    onLastNameChanged(e)
    {
        this.setState({lastName:e.target.value});
    }
    onEmailChanged(e)
    {
        this.setState({email:e.target.value});
    }
    click()
    {
        alert(this.state.wiki + " " + this.state.bacon + " " + this.state.cats)
    }


    render(){
        var newsletter = <div className="container allign-self-center" >
            <div className="content">
                <form className={"subscription" + this.state.class}  >
                    <input className="add-firstName" type="text" placeholder="First Name" onChange={this.onFirstNameChanged.bind(this)}/>
                    <input className="add-firstName" type="text" placeholder="Last Name" onChange={this.onLastNameChanged.bind(this)}/>
                    <input className="add-email"  type="email" placeholder="subscribe@me.now" onChange={this.onEmailChanged.bind(this)}/>
                    <div>
                    <input type="checkbox" id="wiki" name="wiki"  onChange={this.wikiChanged.bind(this)}/>
                    <label for="wiki">Wikipedia articles</label>
                    </div>
                    <div>
                        <input type="checkbox" id="bacon" name="bacon" onChange={this.baconChanged.bind(this)}/>
                        <label htmlFor="bacon">Bacon</label>
                    </div>
                    <div>
                        <input type="checkbox" id="cats" name="cats" onChange={this.catsChanged.bind(this)}/>
                        <label htmlFor="cats">Cat photos</label>
                    </div>
                        <button className="submit-email" type="button" onClick={this.handleClick.bind(this)}>
                            <span className="before-submit">Subscribe</span>
                            <span className="after-submit">Thank you for subscribing!</span>
                        </button>
                </form>
            </div>
            <button onClick={this.click.bind(this)}>click</button>
        </div>;
        return newsletter;
    }
}
ReactDOM.render(
    <App />,
    document.getElementById('react')
)