"use strict";

let changingName = false;
// class EditableNameDisplayer (props) {
//     render() {

//     }
// }
function Header(props) {
    return React.createElement(
        "div",
        null,
        React.createElement(
            "h1",
            null,
            React.createElement(
                "span",
                null,
                "Your name is " + (props.changing ? "" : userName)
            ),
            changingName
                ? React.createElement("input", {
                      defaultValue: 123,
                      onChange: e => {
                          userName = e.target.value;
                          console.log(userName);
                      }
                  })
                : null // @Bug: Locally can set empty name.
        ),
        React.createElement(
            "button",
            {
                onClick: () => {
                    if (changingName) {
                        socket.emit("new name", { userName: userName });
                    }
                    changingName = !changingName;
                    rerender("want to change name");
                }
            },
            changingName ? "Ok" : "Change name"
        )
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
