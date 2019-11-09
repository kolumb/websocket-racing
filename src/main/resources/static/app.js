"use strict";

function NameDiplayer(props) {
    return React.createElement(
        "span",
        { className: "editable-name-displayer" },
        React.createElement("span", null, props.name),
        React.createElement(
            "button",
            { className: "edit-button", onClick: props.onClick },
            "🖉"
        )
    );
}
function NameEditor(props) {
    return React.createElement(
        "span",
        { className: "editable-name-displayer" },
        React.createElement("input", {
            defaultValue: props.name,
            autoFocus: true,
            maxLength: 20,
            onChange: e => {
                userName = e.target.value;
            }
        }),
        React.createElement(
            "button",
            { className: "edit-button", onClick: props.onClick },
            "✓"
        )
    );
}

let changingName = false;
class NameControl extends React.Component {
    constructor(props) {
        super(props);
        this.handleEditClick = this.handleEditClick.bind(this);
        this.handleAcceptClick = this.handleAcceptClick.bind(this);
        this.state = { changingName: false };
    }
    handleEditClick() {
        this.setState({ changingName: true });
    }
    handleAcceptClick() {
        socket.emit("new name", { userName: userName });
        this.setState({ changingName: false });
    }
    render() {
        const changingName = this.state.changingName;
        let nameElement;
        if (changingName) {
            nameElement = React.createElement(NameEditor, {
                name: this.props.name,
                onClick: this.handleAcceptClick
            });
        } else {
            nameElement = React.createElement(NameDiplayer, {
                name: this.props.name,
                onClick: this.handleEditClick
            });
        }
        return nameElement;
    }
}
function Header(props) {
    return React.createElement(
        "h1",
        null,
        React.createElement("span", null, "Your name is "),
        React.createElement(NameControl, { name: userName })
    );
}
function UserListItem(props) {
    return React.createElement(
        "li",
        { className: props.me ? "me" : "" },
        props.name
    );
}
function UserList() {
    let names = [];
    for (let user in users) {
        names.push(users[user]);
    }
    let userElems = names
        .sort((a, b) => a.localeCompare(b))
        .map((name, i) =>
            React.createElement(UserListItem, {
                key: i,
                me: name === userName,
                name: name
            })
        );
    return React.createElement("ul", null, userElems);
}
function App() {
    return React.createElement(
        "div",
        null,
        React.createElement(Header),
        React.createElement(UserList)
    );
}
