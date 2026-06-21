package behaviourdesignpattern;

public class ChainDemo {
    public static void main(String[] args) {

        LeaveRequest leaveRequest = new LeaveRequest("Daryl Dixon", 51);
        LeaveManagerService leaveManagerService = new LeaveManagerService();
        LeaveApprover emp2 = new ProjectManager("Maggie Greene", null);
        LeaveApprover emp1 = new Manager("Rick Grimes",  emp2);
        leaveManagerService.submitLeaveRequest(leaveRequest, emp1);
    }
}

class LeaveRequest {
    String employeeName;
    int leaveDays;

    public LeaveRequest(String employeeName, int leaveDays) {
        this.employeeName = employeeName;
        this.leaveDays = leaveDays;
    }
}

interface LeaveApprover {
    void approveLeave(LeaveRequest leaveRequest);
}

class LeaveManagerService {
    public void submitLeaveRequest(LeaveRequest leaveRequest, LeaveApprover approver) {
        System.out.println(
                leaveRequest.employeeName + " leave request submitted for " + leaveRequest.leaveDays + " days");
        // Assuming we have a way to determine the appropriate approver
        // For now, let's assume the first approver in the chain is the one to handle
        // this

        approver.approveLeave(leaveRequest);
    }
}

// class
class Manager implements LeaveApprover {
    String name;
    LeaveApprover nextApprover;

    public Manager(String name, LeaveApprover nextApprover) {
        this.name = name;
        this.nextApprover = nextApprover;
    }

    @Override
    public void approveLeave(LeaveRequest leaveRequest) {
        if (leaveRequest.leaveDays <= 3) {
            System.out.println("hi aman");
            System.out.println("Leave request for " + leaveRequest.employeeName + " approved by Manager "+this.name);
        } else {
            System.out.println("hi aman1");
            if (nextApprover != null) {
                nextApprover.approveLeave(leaveRequest);
            } else {
                System.out.println("Leave request for " + leaveRequest.employeeName + " cannot be approved");
            }
        }
    }
}

class ProjectManager implements LeaveApprover {
    String name;
    LeaveApprover nextApprover;

    public ProjectManager(String name, LeaveApprover nextApprover) {
        this.name = name;
        this.nextApprover = nextApprover;
    }

    @Override
    public void approveLeave(LeaveRequest leaveRequest) {
        if (leaveRequest.leaveDays >= 4 && leaveRequest.leaveDays <= 7) {
            System.out.println("Leave request for " + leaveRequest.employeeName + " approved by Project Manager " + this.name);
        } else {
            System.out.println("Leave request for " + leaveRequest.employeeName + " cannot be approved");
        }
    }

}