

const UserProfile = (props) => {

    let gender = props.gender;
    gender = gender === "MALE" ? "men" : "women";

    return (
        <div>
            <h1>{props.name} </h1>
            <p>{props.age}</p>
            <img src={`https://randomuser.me/api/portraits/med/${gender}/${props.imgNo}.jpg`}/>
        </div>
    );
}

export default UserProfile;